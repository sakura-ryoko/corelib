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

package com.sakuraryoko.corelib.impl.events.tick;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import com.sakuraryoko.corelib.api.time.ITickHandler;

public class TickManager implements ITickManager
{
	private static final TickManager INSTANCE = new TickManager();
	private final List<ITickHandler> handlers = new ArrayList<>();
	public static TickManager getInstance() { return INSTANCE; }

	@Override
	public void registerTickHandler(ITickHandler handler)
	{
		this.handlers.add(handler);
		this.handlers.sort(
				Comparator.comparingInt(
						inst ->
								ITickHandler.getClampedTickPriority(inst.getTickPriority()))
		);
	}

	@Override
	public void unregisterTickHandler(ITickHandler handler)
	{
		this.handlers.remove(handler);
	}

	@ApiStatus.Internal
	public void onClientTick(Minecraft mc)
	{
		for (ITickHandler handler : this.handlers)
		{
			handler.clientTick(mc);
		}
	}

	@ApiStatus.Internal
	public void onServerTick(MinecraftServer server)
	{
		for (ITickHandler handler : this.handlers)
		{
			handler.serverTick(server);
		}
	}
}
