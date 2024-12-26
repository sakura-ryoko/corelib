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

package com.sakuraryoko.corelib.impl.events.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.GameType;

import com.sakuraryoko.corelib.api.events.IServerEventsDispatch;
import com.sakuraryoko.corelib.impl.CoreLib;

public class ServerEventsManager implements IServerEventManager
{
    private static final ServerEventsManager INSTANCE = new ServerEventsManager();
    public static IServerEventManager getInstance() { return INSTANCE; }
    private final List<IServerEventsDispatch> DISPATCH = new ArrayList<>();

    @Override
    public void registerEventDispatcher(IServerEventsDispatch handler) throws RuntimeException
    {
        if (!this.DISPATCH.contains(handler))
        {
            this.DISPATCH.add(handler);
        }
        else
        {
            throw new RuntimeException("Server Events Dispatcher has already been registered!");
        }
    }

    @ApiStatus.Internal
    public void onStartingInternal(MinecraftServer server)
    {
        CoreLib.debugLog("onStartingInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onStarting(server);
        }
    }

    @ApiStatus.Internal
    public void onStartedInternal(MinecraftServer server)
    {
        CoreLib.debugLog("onStartedInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onStarted(server);
        }
    }

    @ApiStatus.Internal
    public void onReloadCompleteInternal(MinecraftServer server, Collection<String> resources)
    {
        CoreLib.debugLog("onReloadCompleteInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onReloadComplete(server, resources);
        }
    }

    @ApiStatus.Internal
    public void onIntegratedStartedInternal(IntegratedServer server)
    {
        CoreLib.debugLog("onIntegratedStartedInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onIntegratedStarted(server);
        }
    }

    @ApiStatus.Internal
    public void onDedicatedStartedInternal(DedicatedServer server)
    {
        CoreLib.debugLog("onDedicatedStartedInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onDedicatedStarted(server);
        }
    }

    @ApiStatus.Internal
    public void onOpenToLanInternal(IntegratedServer server, GameType mode)
    {
        CoreLib.debugLog("onOpenToLanInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onOpenToLan(server, mode);
        }
    }

    @ApiStatus.Internal
    public void onDedicatedStoppingInternal(DedicatedServer server)
    {
        CoreLib.debugLog("onDedicatedStoppingInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onDedicatedStopping(server);
        }
    }

    @ApiStatus.Internal
    public void onStoppingInternal(MinecraftServer server)
    {
        CoreLib.debugLog("onStoppingInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onStopping(server);
        }
    }

    @ApiStatus.Internal
    public void onStoppedInternal(MinecraftServer server)
    {
        CoreLib.debugLog("onStoppedInternal()");

        for (IServerEventsDispatch dispatch : this.DISPATCH)
        {
            dispatch.onStopped(server);
        }
    }
}
