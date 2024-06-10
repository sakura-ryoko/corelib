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

package com.github.sakuraryoko.corelib.api.events;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;

public class ServerEventsHandler implements IServerEventsManager
{
    private static final ServerEventsHandler INSTANCE = new ServerEventsHandler();
    private final List<IServerEventsDispatch> handlers = new ArrayList<>();
    public static IServerEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerServerEvents(IServerEventsDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterServerEvents(IServerEventsDispatch handler)
    {
        this.handlers.remove(handler);
    }

    @ApiStatus.Internal
    public void onStarting(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStarting(server));
        }
    }

    @ApiStatus.Internal
    public void onStarted(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStarted(server));
        }
    }

    @ApiStatus.Internal
    public void onIntegratedStart(IntegratedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onIntegratedStart(server));
        }
    }

    @ApiStatus.Internal
    public void onOpenToLan(IntegratedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onOpenToLan(server));
        }
    }

    @ApiStatus.Internal
    public void onDedicatedStart(DedicatedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDedicatedStart(server));
        }
    }

    @ApiStatus.Internal
    public void onStopping(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStopping(server));
        }
    }

    @ApiStatus.Internal
    public void onStopped(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStopped(server));
        }
    }
}
