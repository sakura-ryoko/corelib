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

package com.sakuraryoko.corelib.impl.events.client;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus;

//#if MC >= 11902
//$$ import net.minecraft.client.multiplayer.ClientLevel;
//#else
import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.sakuraryoko.corelib.api.events.IClientEventsDispatch;
import com.sakuraryoko.corelib.impl.CoreLib;

@Environment(EnvType.CLIENT)
public class ClientEventsManager implements IClientEventsManager
{
    private static final ClientEventsManager INSTANCE = new ClientEventsManager();
    private final List<IClientEventsDispatch> DISPATCH = new ArrayList<>();
    public static IClientEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerClientEvents(IClientEventsDispatch handler) throws RuntimeException
    {
        if (!this.DISPATCH.contains(handler))
        {
            this.DISPATCH.add(handler);
        }
        else
        {
            throw new RuntimeException("Client Events dispatcher has already been registered!");
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void worldChangePre(@Nullable ClientLevel world)
//#else
    public void worldChangePre(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("worldChangePre()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.worldChangePre(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void worldChangePost(@Nullable ClientLevel world)
//#else
    public void worldChangePost(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("worldChangePost()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.worldChangePost(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onJoining(@Nullable ClientLevel world)
//#else
    public void onJoining(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onJoining()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onJoining(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onOpenConnection(@Nullable ClientLevel world)
//#else
    public void onOpenConnection(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onOpenConnection()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onOpenConnection(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onJoined(@Nullable ClientLevel world)
//#else
    public void onJoined(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onJoined()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onJoined(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onGameJoinPre(@Nullable ClientLevel world)
//#else
    public void onGameJoinPre(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onGameJoinPre()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onGameJoinPre(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onGameJoinPost(@Nullable ClientLevel world)
//#else
    public void onGameJoinPost(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onGameJoinPost()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onGameJoinPost(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onDimensionChangePre(@Nullable ClientLevel world)
//#else
    public void onDimensionChangePre(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onDimensionChangePre()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onDimensionChangePre(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onDimensionChangePost(@Nullable ClientLevel world)
//#else
    public void onDimensionChangePost(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onDimensionChangePost()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onDimensionChangePost(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onDisconnecting(@Nullable ClientLevel world)
//#else
    public void onDisconnecting(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onDisconnecting()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onDisconnecting(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onCloseConnection(@Nullable ClientLevel world)
//#else
    public void onCloseConnection(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onCloseConnection()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onCloseConnection(world));
        }
    }

    @ApiStatus.Internal
//#if MC >= 11902
    //$$ public void onDisconnected(@Nullable ClientLevel world)
//#else
    public void onDisconnected(@Nullable MultiPlayerLevel world)
//#endif
    {
        CoreLib.debugLog("onDisconnected()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.onDisconnected(world));
        }
    }
}
