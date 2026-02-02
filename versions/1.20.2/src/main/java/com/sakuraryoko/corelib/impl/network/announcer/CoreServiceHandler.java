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

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.network.INetworkServiceHandler;
import com.sakuraryoko.corelib.api.network.listener.IClientPacketListener;
import com.sakuraryoko.corelib.api.network.listener.IServerPacketListener;
import com.sakuraryoko.corelib.api.network.packet.INetworkPacket;
import com.sakuraryoko.corelib.api.network.payload.NetworkSide;
import com.sakuraryoko.corelib.api.network.payload.NetworkSidedPayload;
import com.sakuraryoko.corelib.impl.network.NetworkServiceManager;
import com.sakuraryoko.corelib.impl.network.PacketListenerManager;
import com.sakuraryoko.corelib.impl.network.PayloadManager;

public class CoreServiceHandler
		implements INetworkServiceHandler<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket, CoreServicePacket>
{
	private static final CoreServiceHandler INSTANCE = new CoreServiceHandler();
	public static CoreServiceHandler getInstance() { return INSTANCE; }

	private final Server<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket, CoreServicePacket> serverHandler;
	private final Client<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket, CoreServicePacket> clientHandler;

	private CoreServiceHandler()
	{
		this.serverHandler = new Server<>();
		this.clientHandler = new Client<>();
	}

	@Override
	public Server<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket, CoreServicePacket> getServerHandler()
	{
		return this.serverHandler;
	}

	@Override
	public Client<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket, CoreServicePacket> getClientHandler()
	{
		return this.clientHandler;
	}

	@Override
	public CoreServicePacket createHelloPacket()
	{
		String name = "test";
		String address = NetworkServiceManager.getInstance().getLocalAddress().getCanonicalHostName();
		int port = NetworkServiceManager.getInstance().getLocalPort();
		UUID uuid = UUID.randomUUID();

		return new CoreServicePacket(name, address, port, uuid);
	}

	@Override
	public void onRegisterPayloads()
	{
		// todo -- Not needed until 1.20.6+
		PayloadManager.getInstance().registerPayload(new NetworkSidedPayload<>(NetworkSide.C2S, new CoreServicePacket.Payload(this.createHelloPacket())));
		PayloadManager.getInstance().registerPayload(new NetworkSidedPayload<>(NetworkSide.S2C, new CoreServicePacket.Payload(this.createHelloPacket())));
	}

	@Override
	public void registerPacketListeners()
	{
		PacketListenerManager.getInstance().registerC2SPacketListener(this.getServerHandler());
		PacketListenerManager.getInstance().registerS2CPacketListener(this.getClientHandler());
	}

	@Override
	public void unregisterPacketListeners()
	{
		PacketListenerManager.getInstance().unregisterC2SPacketListener(this.getServerHandler());
		PacketListenerManager.getInstance().unregisterS2CPacketListener(this.getClientHandler());
	}

	@Override
	public void sendAsC2SPayload(CoreServicePacket packet, Connection connection)
	{
		this.getClientHandler().sendAsPayload(packet, connection);
	}

	@Override
	public void sendAsS2CPayload(CoreServicePacket packet, Connection connection)
	{
		this.getServerHandler().sendAsPayload(packet, connection);
	}

	@Override
	public void onReceiveC2SPacket(CoreServicePacket packet, ServerPlayer player)
	{
		NetworkServiceManager.LOGGER.warn("[CoreServiceHandler] Received c2s packet from '{}': {}", player.getName().getString(), packet.toString());
	}

	@Override
	public void onReceiveS2CPacket(CoreServicePacket packet)
	{
		NetworkServiceManager.LOGGER.warn("[CoreServiceHandler] Received s2c packet {}", packet.toString());
	}

	public static class Server<C, S, PACKET extends INetworkPacket<C, S>>
			implements IServerPacketListener<C, S, PACKET>
	{
		@Override
		public ResourceLocation getPacketId()
		{
			return CoreServicePacket.PACKET_ID;
		}

		public void sendAsPayload(PACKET packet, ServerPlayer player)
		{
			this.sendPacket(packet.asClientBound(), player);
		}

		public void sendAsPayload(PACKET packet, Connection connection)
		{
			connection.send(packet.asClientBound());
		}

		@Override
		public <T extends PacketListener> boolean receivePacket(Packet<T> packet, ServerPlayer player)
		{
			if (packet instanceof ServerboundCustomPayloadPacket)
			{
				CoreServicePacket result = new CoreServicePacket();
				result = result.fromC2SPacket((ServerboundCustomPayloadPacket) packet);

				if (result != null)
				{
					// Receive
					CoreServiceHandler.getInstance().onReceiveC2SPacket(result, player);
					return true;
				}
			}

			return false;
		}
	}

	public static class Client<C, S, PACKET extends INetworkPacket<C, S>>
			implements IClientPacketListener<C, S, PACKET>
	{
		@Override
		public ResourceLocation getPacketId()
		{
			return CoreServicePacket.PACKET_ID;
		}

		public void sendAsPayload(PACKET packet)
		{
			this.sendPacket(packet.asServerBound());
		}

		public void sendAsPayload(PACKET packet, Connection connection)
		{
			connection.send(packet.asServerBound());
		}

		@Override
		public <T extends PacketListener> boolean receivePacket(Packet<T> packet)
		{
			if (packet instanceof ClientboundCustomPayloadPacket)
			{
				CoreServicePacket result = new CoreServicePacket();
				result = result.fromS2CPacket((ClientboundCustomPayloadPacket) packet);

				if (result != null)
				{
					// Receive
					CoreServiceHandler.getInstance().onReceiveS2CPacket(result);
					return true;
				}
			}

			return false;
		}
	}
}
