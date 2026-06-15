/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
 *
 * CoreLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoreLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CoreLib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sakuraryoko.corelib.impl.network.thread;

import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.sakuraryoko.corelib.api.thread.ThreadExecutorPair;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import com.sakuraryoko.corelib.api.thread.IThreadDaemonHandler;
import com.sakuraryoko.corelib.api.time.ITickHandler;
import com.sakuraryoko.corelib.api.util.MathUtils;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.network.NetworkServiceManager;

public class CoreNetworkThreadHandler implements IThreadDaemonHandler<CoreNetworkThreadTask>, ITickHandler
{
	private static final CoreNetworkThreadHandler INSTANCE = new CoreNetworkThreadHandler();
	public static CoreNetworkThreadHandler getInstance() { return INSTANCE; }

	private static final int MAX_PLATFORM_THREADS = 1;
	private static final float TASK_INTERVAL = 15.0F;
	private boolean useVirtual = false;
	private final String namePrefix = "CoreNetwork Worker Thread";
	private final int threadCount;
	private final ConcurrentHashMap<String, ThreadExecutorPair<CoreNetworkThreadTask>> threadMap;
	private final LinkedBlockingQueue<CoreNetworkThreadTask> queue;
	private final ReentrantLock lock;
	private long lastClientTick;
	private long lastServerTick;
	private boolean forceStop = false;

	private int calculateDefaultSafeThreadCount()
	{
		final int result = this.getThreadCountSafe();
		this.useVirtual = result < 1;
		return MathUtils.clamp(result, 1, MAX_PLATFORM_THREADS);
	}

	private CoreNetworkThreadHandler()
	{
		this.threadCount = MAX_PLATFORM_THREADS;
		this.threadMap = new ConcurrentHashMap<>(this.threadCount, 0.9f, 1);
		this.queue = new LinkedBlockingQueue<>();
		this.lock = new ReentrantLock();
		this.lastClientTick = System.currentTimeMillis();
		this.lastServerTick = System.currentTimeMillis();
	//		this.buildThreadMap();
	}

	private synchronized void buildThreadMap()
	{
		// Only build when empty
		if (this.threadMap.isEmpty())
		{
			this.lock.lock();

			try
			{
				final int count = this.getClampedThreadCount(this.threadCount);

				for (int i = 0; i < count; i++)
				{
					final String name = count > 1 ? this.namePrefix + " " + (i + 1) : this.namePrefix;
					this.threadMap.put(name, this.threadFactory(name, this.useVirtual, new CoreNetworkThreadExecutor()));
				}
			}
			finally
			{
				this.lock.unlock();
			}
		}
	}

	@Override
	public String getName()
	{
		return this.namePrefix;
	}

	private int getClampedThreadCount(final int count)
	{
		return MathUtils.clamp(count, 1, MAX_PLATFORM_THREADS);
	}

	@Override
	public void start()
	{
		if (Reference.EXPERIMENTAL)
		{
			NetworkServiceManager.LOGGER.info("Starting [{}] Worker Daemon threads", this.threadMap.size());
			Set<String> keys = this.threadMap.keySet();

			for (String key : keys)
			{
				ThreadExecutorPair<CoreNetworkThreadTask> pair = this.threadMap.get(key);

				try
				{
					this.safeStart(pair);
				}
				catch (ConcurrentModificationException cme)
				{
					// Busy
				}
				catch (IllegalStateException is)
				{
					this.lock.lock();

					try
					{
						// Terminated
						pair = this.threadFactory(key, this.useVirtual, new CoreNetworkThreadExecutor());
						pair.thread().start();

						synchronized (this.threadMap)
						{
							this.threadMap.replace(key, pair);
						}
					}
					finally
					{
						this.lock.unlock();
					}
				}
				catch (RuntimeException re)
				{
					// Already Running
				}
				catch (Exception ignored) {}
			}
		}
	}

	@Override
	public void stop()
	{
		if (Reference.EXPERIMENTAL)
		{
			NetworkServiceManager.LOGGER.info("Stopping [{}] Worker Daemon threads", this.threadMap.size());
			Set<String> keys = this.threadMap.keySet();

			for (String key : keys)
			{
				ThreadExecutorPair<CoreNetworkThreadTask> pair = this.threadMap.get(key);

				try
				{
					this.safeStop(pair);
				}
				catch (ConcurrentModificationException cme)
				{
					// Busy
					NetworkServiceManager.LOGGER.warn("Thread [{}] is currently busy, and shouldn't be stopped", key);
				}
				catch (IllegalStateException is)
				{
					// Terminated already
				}
				catch (IllegalThreadStateException is)
				{
					// Never started
				}
				catch (Exception ignored) {}
			}
		}
	}

	@Override
	public void reset()
	{
		this.clearTasks();
	}

	@Override
	public void addTask(CoreNetworkThreadTask task)
	{
		if (Reference.EXPERIMENTAL)
		{
			final boolean empty = this.getTaskCount() == 0;

			this.queue.offer(task);

			if (empty)
			{
				this.ensureThreadsAreAlive();
			}
		}
	}

	@Override
	public CoreNetworkThreadTask getNextTask()
	{
		if (Reference.EXPERIMENTAL)
		{
			return this.queue.poll();
		}

		return null;
	}

	public int getTaskCount()
	{
		return this.queue.size();
	}

	@Override
	public boolean hasTasks()
	{
		return !this.queue.isEmpty();
	}

	@Override
	public long getTaskInterval()
	{
		return MathUtils.floor(TASK_INTERVAL * 1000L);
	}

	@Override
	public int getTickPriority()
	{
		return 5;
	}

	@Override
	public void clientTick(Minecraft mc)
	{
		final long now = System.currentTimeMillis();

		if ((now - this.lastClientTick) > this.getTaskInterval())
		{
			if (mc.level != null)
			{
				this.ensureThreadsAreAlive();
			}

			this.lastClientTick = now;
		}
	}

	@Override
	public void serverTick(MinecraftServer server)
	{
		final long now = System.currentTimeMillis();

		if ((now - this.lastServerTick) > this.getTaskInterval())
		{
			this.ensureThreadsAreAlive();
			this.lastServerTick = now;
		}
	}

	private void ensureThreadsAreAlive()
	{
		if (this.hasTasks())
		{
			Set<String> keySet = this.threadMap.keySet();

			for (String key : keySet)
			{
				ThreadExecutorPair<CoreNetworkThreadTask> pair = this.threadMap.get(key);

				try
				{
					this.safeStart(pair);
				}
				catch (IllegalStateException is)
				{
					this.lock.lock();

					try
					{
						// Terminated (Replace)
						pair = this.threadFactory(key, this.useVirtual, new CoreNetworkThreadExecutor());
						pair.thread().start();

						synchronized (this.threadMap)
						{
							this.threadMap.replace(key, pair);
						}
					}
					finally
					{
						this.lock.unlock();
					}
				}
				catch (RuntimeException ignored) {}
			}
		}
	}

	@Override
	public void resetForceStop()
	{
		this.forceStop = false;
	}

	@Override
	public boolean isForceStop()
	{
		return this.forceStop;
	}

	public void clearTasks()
	{
		this.queue.clear();
	}

	@Override
	public void endAll()
	{
		this.forceStop = true;
		this.reset();
		this.stop();
	}

	@Override
	public void close() throws Exception
	{
		this.endAll();
	}
}
