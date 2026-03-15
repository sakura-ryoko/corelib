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

import java.util.ConcurrentModificationException;
//#if MC >= 1.20.6
//$$ import java.time.Duration;
//#else
import com.google.common.util.concurrent.ThreadFactoryBuilder;
//#endif
import org.apache.commons.lang3.math.Fraction;

import com.sakuraryoko.corelib.api.util.MathUtils;
import com.sakuraryoko.corelib.impl.CoreLib;

/**
 * Extend this to create a "Daemon" Instance class that manages a task queue for the Daemon.
 * @param <T> {@link IThreadTaskBase}
 */
public interface IThreadDaemonHandler<T extends IThreadTaskBase> extends AutoCloseable
{
	/**
	 * Get a "Safe" {@link Thread} count; or 1/8 of your system's Core Count.
	 * Note that the number return might be 0, which means that you should
	 * only be using 1 Virtual {@link Thread} Max if your CPU has less than 8 Cores.
	 * @return -
	 */
	default int getThreadCountSafe()
	{
		final int maxThreads = Runtime.getRuntime().availableProcessors();
		final Fraction calc = Fraction.getFraction(maxThreads, 8);
		return MathUtils.clamp(calc.intValue(), 0, maxThreads);
	}

	/**
	 * Default wrapper around building a new {@link Thread}
	 * @param name The name of the new {@link Thread}
	 * @param useVirtual Whether the {@link Thread} should be run Virtually by the JVM
	 * @param executor The {@link IThreadDaemonExecutor} to utilize
	 * @return The newly built {@link Thread}
	 */
	default Thread threadFactory(String name, boolean useVirtual, IThreadDaemonExecutor<T> executor)
	{
		CoreLib.debugLog("IThreadDaemonHandler#threadFactory: '{}' [useVirtual: {}]", name, useVirtual);

		//#if MC >= 1.20.6
		//$$ if (useVirtual)
		//$$ {
			//$$ return Thread.ofVirtual().name(name).unstarted(executor);
		//$$ }

		//$$ return Thread.ofPlatform().name(name).daemon(true).unstarted(executor);
		//#else
		return (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat(name).build().newThread(executor);
		//#endif
	}

	/**
	 * Safely start the {@link Thread} by checking the current state.
	 * @param t The {@link Thread}
	 * @throws RuntimeException The {@link Thread} is Null, or already Running
	 * @throws ConcurrentModificationException The {@link Thread} is in the Blocking state
	 * @throws IllegalStateException The {@link Thread} was terminated, and needs to be replaced.
	 */
	default void safeStart(Thread t) throws RuntimeException
	{
		if (t == null) { throw new RuntimeException(); }
		CoreLib.debugLog("IThreadDaemonHandler#safeStart: '{}' [State: {}]", t.getName(), t.getState().name());

		switch (t.getState())
		{
			case NEW: t.start();
			case TIMED_WAITING: t.interrupt();
			case WAITING: t.interrupt();
			case RUNNABLE: throw new RuntimeException();
			case BLOCKED: throw new ConcurrentModificationException();
			case TERMINATED: throw new IllegalStateException();
		}
	}

	/**
	 * Safely Stop the {@link Thread} by checking the current state.
	 * @param t The {@link Thread}
	 * @throws RuntimeException If the {@link Thread} is Null
	 * @throws IllegalThreadStateException If the {@link Thread} is New and not yet started
	 * @throws ConcurrentModificationException If the {@link Thread} is in a Blocking state
	 * @throws IllegalStateException If the {@link Thread} was Terminated
	 */
	default void safeStop(Thread t) throws RuntimeException
	{
		if (t == null) { throw new RuntimeException(); }
		CoreLib.debugLog("IThreadDaemonHandler#safeStop: '{}' [State: {}]", t.getName(), t.getState().name());

		switch (t.getState())
		{
			case NEW: throw new IllegalThreadStateException();
			case BLOCKED: throw new ConcurrentModificationException();
			case TERMINATED: throw new IllegalStateException();
			default:
			{
				//#if MC >= 1.20.6
				//$$ try
				//$$ {
					//$$ if (t.join(Duration.ofDays(500L)))
					//$$ {
						//$$ this.safeStop(t);
					//$$ }
				//$$ }
				//$$ catch (Exception ignored) {}
				//#else
				try
				{
					t.join(500L);
				}
				catch (InterruptedException e)
				{
					this.safeStop(t);
				}
				//#endif
			}
		}
	}

	/**
	 * Return the {@link Thread} "Prefix" name; such as "MaLiLib Worker Thread ";
	 * which usually ends with a number.  This is important for checking
	 * the Current Running {@link Thread}'s name under {@link IThreadDaemonExecutor}
	 * @return -
	 */
	String getName();

	/**
	 * Start the {@link Thread}(s) -- Which should be done after Game login
	 */
	void start();

	/**
	 * Stop the {@link Thread}(s) -- Which should only be done at Game exit
	 */
	void stop();

	/**
	 * Clear any tasks remaining in the Queue
	 */
	void reset();

	/**
	 * Offer a new task to process
	 * @param newTask {@link IThreadTaskBase}
	 */
	void addTask(T newTask);

	/**
	 * Poll (or Take) the next free task, or NULL
	 * @return {@link IThreadTaskBase}
	 */
	T getNextTask() throws InterruptedException;

	/**
	 * Return the tick interval for managing the queue
	 * @return -
	 */
	long getTaskInterval();

	/**
	 * Return if this has tasks.
	 * @return -
	 */
	boolean hasTasks();

	/**
	 * End Task Execution
	 */
	default void endAll()
	{
		CoreLib.debugLog("IThreadDaemonHandler#endAll()");
		this.reset();
		this.stop();
	}
}
