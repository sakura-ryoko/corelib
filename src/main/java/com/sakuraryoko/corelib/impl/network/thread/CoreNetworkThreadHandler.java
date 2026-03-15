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
	private boolean useVirtual = false;
	private final String namePrefix = "CoreNetworkThreadHandler Worker Thread";
	private static final float TASK_INTERVAL = 15.0F;
	private final int threadCount = this.calculateMaxThreads();
	private final ConcurrentHashMap<String, Thread> threadMap = this.builder();
	private final LinkedBlockingQueue<CoreNetworkThreadTask> queue;
	private long lastClientTick;
	private long lastServerTick;

	private int calculateMaxThreads()
	{
		final int result = this.getThreadCountSafe();
		if (result < 1) { this.useVirtual = true; }

		return MathUtils.clamp(result, 1, MAX_PLATFORM_THREADS);
	}

	private ConcurrentHashMap<String, Thread> builder()
	{
		ConcurrentHashMap<String, Thread> threads = new ConcurrentHashMap<>(this.threadCount, 0.9f, 1);

		for (int i = 0; i < this.threadCount; i++)
		{
			final String name = this.threadCount > 1 ? this.namePrefix+" "+ (i+1) : this.namePrefix;
			threads.put(name, this.threadFactory(name, this.useVirtual, new CoreNetworkThreadExecutor()));
		}

		return threads;
	}

	private CoreNetworkThreadHandler()
	{
		this.queue = new LinkedBlockingQueue<>();
		this.lastClientTick = System.currentTimeMillis();
		this.lastServerTick = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return this.namePrefix;
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
				try
				{
					this.safeStart(this.threadMap.get(key));
				}
				catch (ConcurrentModificationException cme)
				{
					// Busy
				}
				catch (IllegalStateException is)
				{
					// Terminated
					Thread entry = this.threadFactory(key, this.useVirtual, new CoreNetworkThreadExecutor());
					entry.start();

					synchronized (this.threadMap)
					{
						this.threadMap.replace(key, entry);
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
				try
				{
					this.safeStop(this.threadMap.get(key));
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
		this.queue.clear();
	}

	@Override
	public void addTask(CoreNetworkThreadTask task)
	{
		if (Reference.EXPERIMENTAL)
		{
			final boolean wasEmpty = this.queue.isEmpty();
			this.queue.offer(task);

			if (wasEmpty)
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

	protected int getTaskCount()
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
				try
				{
					this.safeStart(this.threadMap.get(key));
				}
				catch (IllegalStateException is)
				{
					// Terminated (Replace)
					Thread entry = this.threadFactory(key, this.useVirtual, new CoreNetworkThreadExecutor());
					entry.start();

					synchronized (this.threadMap)
					{
						this.threadMap.replace(key, entry);
					}
				}
				catch (RuntimeException ignored) {}
			}
		}
	}

	@Override
	public void close() throws Exception
	{
		this.endAll();
	}
}
