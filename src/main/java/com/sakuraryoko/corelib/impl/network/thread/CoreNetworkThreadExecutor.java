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

import java.util.concurrent.atomic.AtomicBoolean;

import com.sakuraryoko.corelib.api.thread.IThreadDaemonExecutor;
import com.sakuraryoko.corelib.impl.network.NetworkServiceManager;

public class CoreNetworkThreadExecutor implements IThreadDaemonExecutor<CoreNetworkThreadTask>
{
	private final AtomicBoolean running = new AtomicBoolean(true);
	private long lastTick = System.currentTimeMillis();

	@Override
	public boolean isRunning()
	{
		return this.running.get();
	}

	@Override
	public void start()
	{
		this.running.set(true);
	}

	@Override
	public void stop()
	{
		this.running.set(false);
	}

	@Override
	public void run()
	{
		NetworkServiceManager.LOGGER.info("CoreNetworkThreadExecutor: begin");

		while (this.isRunning())
		{
			final long now = System.currentTimeMillis();

			if ((now - this.lastTick) > 15000L)
			{
				NetworkServiceManager.LOGGER.error("CoreNetworkThreadExecutor: tick()");
				this.lastTick = now;
			}

			try
			{
				CoreNetworkThreadTask task = CoreNetworkThreadHandler.getInstance().getNextTask();

				if (task != null)
				{
					this.processTask(task);
				}
			}
			catch (Exception e)
			{
				NetworkServiceManager.LOGGER.error("CoreNetworkThreadExecutor: Exception running task; {}", e.getLocalizedMessage());
			}
		}

		NetworkServiceManager.LOGGER.info("CoreNetworkThreadExecutor: exit");
	}

	@Override
	public void processTask(CoreNetworkThreadTask task) throws InterruptedException
	{
		NetworkServiceManager.LOGGER.info("CoreNetworkThreadExecutor: processTask()");
		task.run();
	}
}
