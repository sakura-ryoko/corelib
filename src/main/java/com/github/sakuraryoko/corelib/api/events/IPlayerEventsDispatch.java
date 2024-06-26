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
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
//#if MC >= 12002
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface IPlayerEventsDispatch
{
    void onConnection(SocketAddress addr, GameProfile profile, @Nullable Text result);

    //#if MC >= 12002
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
