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

package com.sakuraryoko.corelib.api.time;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import com.sakuraryoko.corelib.api.util.MathUtils;

public interface ITickHandler
{
	/**
	 * Set the tick priority.  The lower the number the better;
	 * but please try to keep this value between 1-1000.
	 * @return Tick Priority
	 */
	default int getTickPriority() { return 500; }

	@ApiStatus.Internal
	static int getClampedTickPriority(int value)
	{
		return MathUtils.clamp(value, 1, 1000);
	}

	/**
	 * Executes at the end of every Client Tick; if we have a Client instance running;
	 * and if there is time left out of ~50 MSPT.  Otherwise, it will not be called.
	 * @param mc    The Minecraft object instance
	 */
	void clientTick(Minecraft mc);

	/**
	 * Executes at the end of every Server Tick; if we have a Server instance running;
	 * and if there is time left out of ~50 MSPT.  Otherwise, it will not be called.
	 * @param server    The Minecraft Server object instance
	 */
	void serverTick(MinecraftServer server);
}
