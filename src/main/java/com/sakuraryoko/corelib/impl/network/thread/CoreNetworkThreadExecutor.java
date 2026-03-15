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
import com.sakuraryoko.corelib.api.util.MathUtils;
import com.sakuraryoko.corelib.impl.network.NetworkServiceManager;

public class CoreNetworkThreadExecutor implements IThreadDaemonExecutor<CoreNetworkThreadTask>
{
	private final AtomicBoolean running = new AtomicBoolean(true);
	private final AtomicBoolean paused = new AtomicBoolean(false);
	private final long sleepTime;
	private final float sleepDelay;
	private long lastTaskTime;

	public CoreNetworkThreadExecutor()
	{
		this(600000L);  // 10 min
	}

	public CoreNetworkThreadExecutor(long sleepTime)
	{
		this.sleepTime = MathUtils.clamp(sleepTime, 60000L, Long.MAX_VALUE); // 1 min
		this.sleepDelay = 0.75F;     // <1-second sleep delay (Must be 1/2 tick rate)
	}

	@Override
	public boolean isRunning()
	{
		return this.running.get();
	}

	@Override
	public boolean isPaused()
	{
		return this.paused.get();
	}

	@Override
	public void start()
	{
		if (!this.isRunning())
		{
			NetworkServiceManager.LOGGER.info("Executor: Starting");
			if (this.isPaused())
			{
				this.paused.set(false);
			}

			this.running.set(true);
		}

		this.run();
	}

	@Override
	public void interrupt(InterruptedException interrupt)
	{
		NetworkServiceManager.LOGGER.info("Executor: Interrupt Signal: {}", interrupt.getLocalizedMessage() != null
		                                                   ? interrupt.getLocalizedMessage()  // This is null sometimes?
		                                                   : "received interrupt signal");
		if (this.isPaused() || !this.isRunning())
		{
			this.resume();
		}
	}

	@Override
	public void pause()
	{
		NetworkServiceManager.LOGGER.info("Executor: Pausing");
		this.paused.set(true);
	}

	@Override
	public void resume()
	{
		if (this.isPaused())
		{
			NetworkServiceManager.LOGGER.info("Executor: Resuming");
			this.paused.set(false);
		}

		this.start();
	}

	@Override
	public void stop()
	{
		NetworkServiceManager.LOGGER.info("Executor: Stopping");
		if (!this.isPaused())
		{
			this.paused.set(true);
		}
		if (this.isRunning())
		{
			this.running.set(false);
		}
	}

	@Override
	public long sleepTime()
	{
		return this.sleepTime;
	}

	@Override
	public String getName()
	{
		return CoreNetworkThreadHandler.getInstance().getName();
	}

	@Override
	public boolean hasTasks()
	{
		return CoreNetworkThreadHandler.getInstance().hasTasks();
	}

	@Override
	public void run()
	{
		NetworkServiceManager.LOGGER.info("CoreNetworkThreadExecutor: begin");

		if (!this.isCorrectThread()) { return; }
		this.lastTaskTime = System.currentTimeMillis();
		NetworkServiceManager.LOGGER.info("Executor: Running: [{}/{}]", this.isRunning(), this.isPaused());

		while (this.isRunning())
		{
			if (this.isPaused() && this.hasTasks())
			{
				this.resume();
			}
			else if (!this.isPaused() && this.loopSafe())
			{
				this.paused.set(true);
				this.sleep();
				return;
			}
		}
	}

	@Override
	public boolean loopSafe()
	{
		try
		{
			CoreNetworkThreadTask task = this.takeNextTask();

			if (task != null)
			{
				this.processTask(task);
				this.lastTaskTime = System.currentTimeMillis();
				return false;
			}
		}
		catch (InterruptedException e)
		{
			this.interrupt(e);
		}
		catch (Exception err)
		{
			NetworkServiceManager.LOGGER.error("loopSafe: Exception: {}", err.getLocalizedMessage());
		}

		return this.shouldPause();
	}

	@Override
	public boolean shouldPause()
	{
		if (this.hasTasks()) { return false; }
		return (System.currentTimeMillis() - this.lastTaskTime) > (this.sleepDelay * 1000L);
	}

	private CoreNetworkThreadTask takeNextTask() throws InterruptedException
	{
		return CoreNetworkThreadHandler.getInstance().getNextTask();
	}

	@Override
	public void processTask(CoreNetworkThreadTask task) throws InterruptedException
	{
		NetworkServiceManager.LOGGER.info("CoreNetworkThreadExecutor: processTask()");
		task.run();
	}
}
