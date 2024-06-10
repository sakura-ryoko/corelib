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
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
//#if MC >= 12006
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface IPlayerEventsDispatch
{
    void onConnection(SocketAddress addr, GameProfile profile, @Nullable Text result);

    //#if MC >= 12006
    //$$ void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection, ConnectedClientData clientData);
    //#else
    void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection);
    //#endif

    void onPlayerJoinPost(ServerPlayerEntity player);

    void onPlayerRespawn(ServerPlayerEntity newPlayer);

    void onPlayerLeave(ServerPlayerEntity player);

    void onDisconnectAll();

    void onSetViewDistance(int distance);

    void onSetSimulationDistance(int distance);
}
