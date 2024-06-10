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

import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
//#if MC >= 12006
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerEventsHandler implements IPlayerEventsManager
{
    private static final PlayerEventsHandler INSTANCE = new PlayerEventsHandler();
    private final List<IPlayerEventsDispatch> handlers = new ArrayList<>();
    public static IPlayerEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerPlayerEvents(IPlayerEventsDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterPlayerEvents(IPlayerEventsDispatch handler)
    {
        this.handlers.remove(handler);
    }

    @ApiStatus.Internal
    public void onConnection(SocketAddress addr, GameProfile profile, @Nullable Text result)
    {
        if (result == null)
        {
            CoreLog.debug("onConnection: connection from {} // {}", addr.toString(), profile.getId().toString());
        }
        else
        {
//#if MC >= 12004
            //$$ CoreLog.debug("onConnection: connection from {} // {} --> result: {}", addr.toString(), profile.getId().toString(), result.getLiteralString());
//#else
            CoreLog.debug("onConnection: connection from {} // {} --> result: {}", addr.toString(), profile.getId().toString(), result.getString());
//#endif
        }

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onConnection(addr, profile, result));
        }
    }

@ApiStatus.Internal
//#if MC >= 12006
    //$$ public void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection, ConnectedClientData clientData)
    //$$ {
        //$$ CoreLog.debug("onPlayerJoinPre: {} is joining.", player.getName().getLiteralString());

        //$$ if (!this.handlers.isEmpty())
        //$$ {
            //$$ this.handlers.forEach((handler) -> handler.onPlayerJoinPre(player, clientConnection, clientData));
        //$$ }
    //$$ }
//#else
    public void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection)
    {
        CoreLog.debug("onPlayerJoinPre: {} is joining.", player.getName().toString());

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerJoinPre(player, clientConnection));
        }
    }
//#endif

    @ApiStatus.Internal
    public void onPlayerJoinPost(ServerPlayerEntity player)
    {
//#if MC >= 12004
        //$$ CoreLog.debug("onPlayerJoinPost: {} has joined.", player.getName().getLiteralString());
//#else
        CoreLog.debug("onPlayerJoinPost: {} has joined.", player.getName().toString());
//#endif

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerJoinPost(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerRespawn(ServerPlayerEntity player)
    {
//#if MC >= 12004
        //$$ CoreLog.debug("onPlayerRespawn: {} has respawned.", player.getName().getLiteralString());
//#else
        CoreLog.debug("onPlayerRespawn: {} has respawned.", player.getName().toString());
//#endif

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerRespawn(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerLeave(ServerPlayerEntity player)
    {
//#if MC >= 12004
        //$$ CoreLog.debug("onPlayerLeave: {} has left.", player.getName().getLiteralString());
//#else
        CoreLog.debug("onPlayerLeave: {} has left.", player.getName().getString());
//#endif

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerLeave(player));
        }
    }

    @ApiStatus.Internal
    public void onDisconnectAll()
    {
        CoreLog.debug("onDisconnectAll: disconnect all clients.");

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IPlayerEventsDispatch::onDisconnectAll);
        }
    }

    @ApiStatus.Internal
    public void onSetViewDistance(int distance)
    {
        CoreLog.debug("onSetViewDistance: setViewDistance to {}", distance);

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onSetViewDistance(distance));
        }
    }

    @ApiStatus.Internal
    public void onSetSimulationDistance(int distance)
    {
        CoreLog.debug("onSetSimulationDistance: setSimulationDistance to {}", distance);

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onSetSimulationDistance(distance));
        }
    }
}
