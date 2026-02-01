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

package com.sakuraryoko.corelib.impl.network.announcer;

import java.util.UUID;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.network.IClientPacketListener;
import com.sakuraryoko.corelib.api.network.IServerPacketListener;
import com.sakuraryoko.corelib.impl.network.NetworkServiceManager;

public class CoreServiceHandler
{
	private static final CoreServiceHandler INSTANCE = new CoreServiceHandler();
	public static CoreServiceHandler getInstance() { return INSTANCE; }

	private final Server serverHandler;
	private final Client clientHandler;

	private CoreServiceHandler()
	{
		this.serverHandler = new Server();
		this.clientHandler = new Client();
	}

	public ResourceLocation getPacketId()
	{
		return CoreServicePacket.PACKET_ID;
	}

	public Server getServerHandler()
	{
		return this.serverHandler;
	}

	public Client getClientHandler()
	{
		return this.clientHandler;
	}

	public CoreServicePacket createHelloPacket()
	{
		String name = "test";
		String address = NetworkServiceManager.getInstance().getLocalAddress().getCanonicalHostName();
		int port = NetworkServiceManager.getInstance().getLocalPort();
		UUID uuid = UUID.randomUUID();

		return new CoreServicePacket(name, address, port, uuid);
	}

	public static class Server implements IServerPacketListener
	{
		@Override
		public ResourceLocation getPacketId()
		{
			return CoreServicePacket.PACKET_ID;
		}

		@Override
		public <T extends PacketListener> void receivePacket(Packet<T> packet, ServerPlayer player)
		{
			if (packet instanceof CoreServicePacket.C2SPayload)
			{
				CoreServicePacket.C2SPayload payload = (CoreServicePacket.C2SPayload) packet;
				CoreServicePacket result = payload.toPacket();

				// Receive
				NetworkServiceManager.LOGGER.warn("[CoreServiceHandler] Received c2s packet from '{}': {}", player.getName().getString(), result.toString());
			}
		}
	}

	public static class Client implements IClientPacketListener
	{
		@Override
		public ResourceLocation getPacketId()
		{
			return CoreServicePacket.PACKET_ID;
		}

		@Override
		public <T extends PacketListener> void receivePacket(Packet<T> packet)
		{
			if (packet instanceof CoreServicePacket.S2CPayload)
			{
				CoreServicePacket.S2CPayload payload = (CoreServicePacket.S2CPayload) packet;
				CoreServicePacket result = payload.toPacket();

				// Receive
				NetworkServiceManager.LOGGER.warn("[CoreServiceHandler] Received s2c packet {}", result.toString());
			}
		}
	}
}
