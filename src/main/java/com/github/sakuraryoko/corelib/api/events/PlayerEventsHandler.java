/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import com.github.sakuraryoko.corelib.util.CoreLog;
import com.github.sakuraryoko.corelib.util.PlayerNameUtil;
import org.jetbrains.annotations.ApiStatus;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
//#if MC >= 12002
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
//#if MC >= 12002
    //$$ public void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection, ConnectedClientData clientData)
//#else
    public void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection)
//#endif
    {
        CoreLog.debug("onPlayerJoinPre: {} is joining.", PlayerNameUtil.getPlayerName(player));
        
        if (!this.handlers.isEmpty())
        {
            //#if MC >= 12002
            //$$ this.handlers.forEach((handler) -> handler.onPlayerJoinPre(player, clientConnection, clientData));
            //#else
            this.handlers.forEach((handler) -> handler.onPlayerJoinPre(player, clientConnection));
            //#endif
        }
    }

    @ApiStatus.Internal
    public void onPlayerJoinPost(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerJoinPost: {} has joined.", PlayerNameUtil.getPlayerName(player));

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerJoinPost(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerRespawn(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerRespawn: {} has respawned.", PlayerNameUtil.getPlayerName(player));

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerRespawn(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerLeave(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerLeave: {} has left.", PlayerNameUtil.getPlayerName(player));

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
