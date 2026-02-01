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
 * Extend this to create a "Daemon" Instance class that manages a task queue for the Daemon.
 * @param <T> {@link IThreadTaskBase}
 */
public interface IThreadDaemonHandler<T extends IThreadTaskBase> extends AutoCloseable
{
	/**
	 * Meant to delay the start of the thread
	 */
	void start();

	/**
	 * Stop the thread
	 */
	void stop();

	/**
	 * Stop/Start
	 */
	void reset();

	/**
	 * Add a new task to process
	 * @param newTask {@link IThreadTaskBase}
	 */
	void addTask(T newTask);

	/**
	 * Pool the next free task, or NULL
	 * @return {@link IThreadTaskBase}
	 */
	T getNextTask();

	/**
	 * Return the tick interval for managing the queue
	 * @return ()
	 */
	long getTaskInterval();
}
