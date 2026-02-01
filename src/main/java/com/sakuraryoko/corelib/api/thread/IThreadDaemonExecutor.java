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

/**
 * This interface is for creating a Thread Executor class --
 * - The thread's main loop via {@link Runnable}
 * @param <T> {@link IThreadTaskBase}
 */
public interface IThreadDaemonExecutor<T extends IThreadTaskBase> extends Runnable
{
	/**
	 * Return the "Running" status of the Thread (Use an {@link java.util.concurrent.atomic.AtomicBoolean})
	 * @return ()
	 */
	boolean isRunning();

	/**
	 * Starts the Running process.
	 */
	void start();

	/**
	 * Stops the running process
	 */
	void stop();

	/**
	 * Executes a task that is polled by the {@link java.util.Queue}
	 * @param task {@link IThreadTaskBase}
	 * @throws InterruptedException ()
	 */
	void processTask(T task) throws InterruptedException;
}
