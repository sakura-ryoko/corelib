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

package com.sakuraryoko.corelib.api.network.listener;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.network.packet.INetworkPacket;

/**
 * The Server side Packet Listener (Sending/Receiving) instance.
 *
 * @param <C2S>         The Server bound payload {@link INetworkPacket} reference
 * @param <S2C>         The Client bound payload {@link INetworkPacket} reference
 * @param <PACKET>      The {@link INetworkPacket} instance reference
 */
public interface IServerPacketListener<C2S, S2C, PACKET extends INetworkPacket<C2S, S2C>>
{
	ResourceLocation getPacketId();

	void sendAsPayload(PACKET packet, ServerPlayer player);

	void sendAsPayload(PACKET packet, Connection connection);

	default <T extends PacketListener> void sendPacket(Packet<T> packet, ServerPlayer player)
	{
		if (player != null && player.connection != null)
		{
			player.connection.send(packet);
		}
	}

	<T extends PacketListener> boolean receivePacket(Packet<T> packet, ServerPlayer player);
}
