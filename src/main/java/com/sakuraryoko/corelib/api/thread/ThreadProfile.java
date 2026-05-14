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

public class ThreadProfile
{
	private final String name;
	private final int maxTicks;
	private final long yieldTime;

	public ThreadProfile(final String name, int maxTicks, long yieldTime)
	{
		this.name = name;
		this.maxTicks = maxTicks;
		this.yieldTime = yieldTime;
	}

	public String name()
	{
		return this.name;
	}

	public int maxTicks()
	{
		return this.maxTicks;
	}

	public long yieldTime()
	{
		return this.yieldTime;
	}
}
