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

package com.sakuraryoko.corelib.impl.events.players;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.events.IPlayerEventsDispatch;
import com.sakuraryoko.corelib.impl.CoreLib;

public class PlayerEventsManager implements IPlayerEventsManager
{
    private static final PlayerEventsManager INSTANCE = new PlayerEventsManager();
    private final List<IPlayerEventsDispatch> DISPATCH = new ArrayList<>();

    public static IPlayerEventsManager getInstance() {return INSTANCE;}

    @Override
    public void registerPlayerEvents(IPlayerEventsDispatch dispatch) throws RuntimeException
    {
        if (!this.DISPATCH.contains(dispatch))
        {
            this.DISPATCH.add(dispatch);
        }
        else
        {
            throw new RuntimeException("Player Events Dispatch is already registered!");
        }
    }

    @ApiStatus.Internal
    public void onConnection(SocketAddress addr, GameProfile profile, @Nullable Component result)
    {
        if (result == null)
        {
//#if MC >= 12110
	        //$$ CoreLib.debugLog("onConnection: connection from {} // {}", addr.toString(), profile.id().toString());
        //$$ }
        //$$ else
        //$$ {
	        //$$ CoreLib.debugLog("onConnection: connection from {} // {} --> result: {}", addr.toString(), profile.id().toString(), result.getString());
//#else
            CoreLib.debugLog("onConnection: connection from {} // {}", addr.toString(), profile.getId().toString());
        }
        else
        {
            CoreLib.debugLog("onConnection: connection from {} // {} --> result: {}", addr.toString(), profile.getId().toString(), result.getString());
//#endif
        }

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onConnection(addr, profile, result));
        }
    }

    @ApiStatus.Internal
    public void onCreatePlayer(ServerPlayer player, GameProfile profile)
    {
        CoreLib.debugLog("onCreatePlayer: {}", player.getName().getString());

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onCreatePlayer(player, profile));
        }
    }

    @ApiStatus.Internal
    public void onPlayerJoinPre(ServerPlayer player, Connection connection)
    {
        CoreLib.debugLog("onPlayerJoinPre: {} is joining.", player.getName().getString());

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onPlayerJoinPre(player, connection));
        }
    }

    @ApiStatus.Internal
    public void onPlayerJoinPost(ServerPlayer player, Connection connection)
    {
        CoreLib.debugLog("onPlayerJoinPost: {} has joined.", player.getName().getString());

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onPlayerJoinPost(player, connection));
        }
    }

    @ApiStatus.Internal
    public void onPlayerRespawn(ServerPlayer newPlayer)
    {
        CoreLib.debugLog("onPlayerRespawn: {} has respawned.", newPlayer.getName().getString());

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onPlayerRespawn(newPlayer));
        }
    }

    @ApiStatus.Internal
    public void onPlayerLeave(ServerPlayer player)
    {
        CoreLib.debugLog("onPlayerLeave: {} has left.", player.getName().getString());

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onPlayerLeave(player));
        }
    }

    @ApiStatus.Internal
    public void onDisconnectAll()
    {
        CoreLib.debugLog("onDisconnectAll: disconnect all clients.");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach(IPlayerEventsDispatch::onDisconnectAll);
        }
    }

    @ApiStatus.Internal
    public void onSetViewDistance(int distance)
    {
        CoreLib.debugLog("onSetViewDistance: setViewDistance to {}", distance);

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onSetViewDistance(distance));
        }
    }

    @ApiStatus.Internal
    public void onSetSimulationDistance(int distance)
    {
        CoreLib.debugLog("onSetSimulationDistance: setSimulationDistance to {}", distance);

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((dispatch) -> dispatch.onSetSimulationDistance(distance));
        }
    }
}
