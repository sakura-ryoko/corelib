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

package com.sakuraryoko.corelib.api.thread;

import com.sakuraryoko.corelib.impl.CoreLib;

/**
 * This interface is for creating a Thread Executor class --
 * - The thread's main loop via {@link Runnable}
 * @param <T> {@link IThreadTaskBase}
 */
public interface IThreadDaemonExecutor<T extends IThreadTaskBase> extends Runnable
{
	/**
	 * Set the {@link Thread} Max Sleep time.
	 * @return -
	 */
	long sleepTime();

	/**
	 * Get this {@link Thread} Prefix name, so we can match it using 'isCorrectThread'
	 * @return -
	 */
	String getName();

	/**
	 * Get the "Current" {@link Thread} name.
	 * @return -
	 */
	default String currentThreadName()
	{
		return Thread.currentThread().getName();
	}

	/**
	 * Return the "Running" status of the {@link Thread} (Use an {@link java.util.concurrent.atomic.AtomicBoolean})
	 * @return -
	 */
	boolean isRunning();

	/**
	 * Return the "Paused" status of the {@link Thread} (Use an {@link java.util.concurrent.atomic.AtomicBoolean})
	 * @return -
	 */
	boolean isPaused();

	/**
	 * Starts the {@link Thread} Running process.
	 */
	void start();

	/**
	 * Stops the {@link Thread} running process
	 */
	void stop();

	/**
	 * Temporarily Pause {@link Thread} execution.
	 */
	void pause();

	/**
	 * Resume {@link Thread} from Pause.
	 */
	void resume();

	/**
	 * Return if the {@link IThreadDaemonHandler} has tasks to process.
	 * @return -
	 */
	boolean hasTasks();

	/**
	 * Run a "Safe" Loop, and return if it should sleep the {@link Thread} if there are no Tasks to run.
	 * @return -
	 */
	boolean loopSafe();

	/**
	 * Return whether the {@link Thread} should sleep.
	 * @return -
	 */
	default boolean shouldPause()
	{
		return !this.hasTasks();
	}

	/**
	 * Send the {@link IThreadDaemonExecutor} the "interrupt" signal.
	 * @param interrupt -
	 */
	void interrupt(InterruptedException interrupt);

	/**
	 * Executes a task that is polled by the {@link java.util.Queue}
	 * @param task {@link IThreadTaskBase}
	 * @throws InterruptedException -
	 */
	void processTask(T task) throws InterruptedException;

	/**
	 * Return if the current {@link Thread} is correct, and not randomly called from Minecraft's Rendering Thread.
	 * @return -
	 */
	default boolean isCorrectThread()
	{
		return this.currentThreadName().toLowerCase().contains(this.getName().toLowerCase());
	}

	/**
	 * Sleeps the current running {@link Thread}, if we are on the Correct {@link Thread},
	 * and if it is Running for 'sleepTime()' milliseconds.
	 */
	default void sleep()
	{
		this.sleep(this.sleepTime());
	}

	/**
	 * Sleeps the current running {@link Thread}, if we are on the Correct {@link Thread},
	 * and if it is Running for 'millis' milliseconds.
	 */
	default void sleep(long millis)
	{
		if (this.isCorrectThread() && this.isRunning())
		{
			try
			{
				if (!this.isPaused())
				{
					this.pause();
				}

				CoreLib.debugLog("IThreadDaemonExecutor#Executor: sleeping: '{}' for [{}]", this.currentThreadName(), this.sleepTime());
				Thread.sleep(millis);
			}
			catch (InterruptedException e)
			{
				this.interrupt(e);
			}
			finally
			{
				this.resume();
			}
		}
	}
}
