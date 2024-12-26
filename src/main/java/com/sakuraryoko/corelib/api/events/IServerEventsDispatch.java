/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.api.events;

import java.util.Collection;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.GameType;

public interface IServerEventsDispatch
{
    void onStarting(MinecraftServer server);
    void onStarted(MinecraftServer server);
    void onReloadComplete(MinecraftServer server, Collection<String> resources);
    void onDedicatedStarted(DedicatedServer server);
    void onIntegratedStarted(IntegratedServer server);
    void onOpenToLan(IntegratedServer server, GameType mode);
    void onDedicatedStopping(DedicatedServer server);
    void onStopping(MinecraftServer server);
    void onStopped(MinecraftServer server);
}
