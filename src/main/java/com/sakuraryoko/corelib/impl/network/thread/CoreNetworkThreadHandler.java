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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import com.sakuraryoko.corelib.api.thread.IThreadDaemonHandler;
import com.sakuraryoko.corelib.api.time.ITickHandler;
import com.sakuraryoko.corelib.impl.Reference;

public class CoreNetworkThreadHandler implements IThreadDaemonHandler<CoreNetworkThreadTask>, ITickHandler
{
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("CoreNetworkThreadHandler-%d").build();
	private static final CoreNetworkThreadHandler INSTANCE = new CoreNetworkThreadHandler();
	public static CoreNetworkThreadHandler getInstance() { return INSTANCE; }

	private final Thread thread;
	private final CoreNetworkThreadExecutor executor;
	private final LinkedBlockingQueue<CoreNetworkThreadTask> queue;
	private final int taskInterval;
	private long lastClientTick;
	private long lastServerTick;

	public CoreNetworkThreadHandler()
	{
		this.executor = new CoreNetworkThreadExecutor();
		this.thread = THREAD_FACTORY.newThread(this.executor);
		this.queue = new LinkedBlockingQueue<>();
		this.taskInterval = 15;
		this.lastClientTick = System.currentTimeMillis();
		this.lastServerTick = System.currentTimeMillis();
	}

	@Override
	public void start()
	{
		if (Reference.EXPERIMENTAL)
		{
			this.executor.start();
			this.thread.start();
		}
	}

	@Override
	public void stop()
	{
		this.thread.interrupt();
	}

	@Override
	public void reset()
	{
		this.queue.clear();
	}

	@Override
	public void addTask(CoreNetworkThreadTask newTask)
	{
		if (Reference.EXPERIMENTAL)
		{
			this.queue.add(newTask);
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

	@Override
	public long getTaskInterval()
	{
		return this.taskInterval * 1000L;
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
			this.lastClientTick = now;
		}
	}

	@Override
	public void serverTick(MinecraftServer server)
	{
		final long now = System.currentTimeMillis();

		if ((now - this.lastServerTick) > this.getTaskInterval())
		{
			this.lastServerTick = now;
		}
	}

	@Override
	public void close() throws Exception
	{
		this.reset();
		this.executor.stop();
		this.thread.join();
	}
}
