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

public enum ThreadProfiles
{
	MAX			(new ThreadProfile("max", 	32, 20L)),
	DEFAULT		(new ThreadProfile("default", 16, 50L)),
	MIN			(new ThreadProfile("min", 	8,  100L)),
	POTATO		(new ThreadProfile("potato", 	4,  150L)),
	;

	private final ThreadProfile profile;

	ThreadProfiles(ThreadProfile profile)
	{
		this.profile = profile;
	}

	public ThreadProfile profile()
	{
		return this.profile;
	}

	public static ThreadProfiles fromString(String string)
	{
		for (ThreadProfiles entry : values())
		{
			if (entry.profile().name().equalsIgnoreCase(string))
			{
				return entry;
			}
		}

		return ThreadProfiles.DEFAULT;
	}
}
