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

package com.sakuraryoko.corelib.api.network;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.network.listener.IClientPacketListener;
import com.sakuraryoko.corelib.api.network.listener.IServerPacketListener;
import com.sakuraryoko.corelib.api.network.packet.INetworkPacket;

/**
 * Create the Network Service Handler
 *
 * @param <C2S>         The Server bound C2S {@link com.sakuraryoko.corelib.api.network.payload.INetworkPayload} reference
 * @param <S2C>         The Client bound S2C {@link com.sakuraryoko.corelib.api.network.payload.INetworkPayload} reference
 * @param <PACKET>      The {@link INetworkPacket} reference
 */
public interface INetworkServiceHandler<C2S, S2C, PACKET extends INetworkPacket<C2S, S2C>>
{
	IServerPacketListener<C2S, S2C, PACKET> getServerHandler();

	IClientPacketListener<C2S, S2C, PACKET> getClientHandler();

	PACKET createHelloPacket();

	void onRegisterPayloads();

	void registerPacketListeners();

	void unregisterPacketListeners();

	void sendAsC2SPayload(PACKET packet, Connection connection);

	void sendAsS2CPayload(PACKET packet, Connection connection);

	void onReceiveC2SPacket(PACKET packet, ServerPlayer player);

	void onReceiveS2CPacket(PACKET packet);
}
