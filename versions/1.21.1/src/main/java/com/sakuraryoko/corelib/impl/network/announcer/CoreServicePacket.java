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
import org.jspecify.annotations.NonNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.corelib.api.network.packet.INetworkPacket;
import com.sakuraryoko.corelib.api.network.payload.INetworkPayload;
import com.sakuraryoko.corelib.impl.Reference;

public class CoreServicePacket implements INetworkPacket<ServerboundCustomPayloadPacket, ClientboundCustomPayloadPacket>
{
	public static final ResourceLocation PACKET_ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "network_service");
	private String serviceName;
	private String serviceAddress;
	private int servicePort;
	private UUID serviceUUID;

	public CoreServicePacket()
	{
		this("", "", -1, UUID.randomUUID());
	}

	public CoreServicePacket(String name, String address, int port, UUID uuid)
	{
		this.serviceName = name;
		this.serviceAddress = address;
		this.servicePort = port;
		this.serviceUUID = uuid;
	}

	private CoreServicePacket(FriendlyByteBuf buf)
	{
		this.fromByteBuf(buf);
	}

	public boolean isEmpty()
	{
		return this.getServiceName().isEmpty() || this.getServiceAddress().isEmpty();
	}

	public String getServiceName()
	{
		return this.serviceName;
	}

	public String getServiceAddress()
	{
		return this.serviceAddress;
	}

	public int getServicePort()
	{
		return this.servicePort;
	}

	public UUID getServiceUUID()
	{
		return this.serviceUUID;
	}

	private void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	private void setServiceAddress(String serviceAddress)
	{
		this.serviceAddress = serviceAddress;
	}

	private void setServicePort(int servicePort)
	{
		this.servicePort = servicePort;
	}

	private void setServiceUUID(UUID serviceUUID)
	{
		this.serviceUUID = serviceUUID;
	}

	@Override
	public String toString()
	{
		return "CoreServicePacket["
				+ this.serviceName
				+ ", "
				+ this.serviceAddress
				+ ", "
				+ this.servicePort
				+ ", "
				+ this.serviceUUID.toString()
				+ "]";
	}

	@Override
	public void toByteBuf(FriendlyByteBuf packet)
	{
		packet.writeUtf(this.serviceName);
		packet.writeUtf(this.serviceAddress);
		packet.writeInt(this.servicePort);
		packet.writeUUID(this.serviceUUID);
	}

	@Override
	public ServerboundCustomPayloadPacket toC2SPacket()
	{
		return new ServerboundCustomPayloadPacket(new Payload(this));
	}

	@Override
	public ClientboundCustomPayloadPacket toS2CPacket()
	{
		return new ClientboundCustomPayloadPacket(new Payload(this));
	}

	@Override
	public ServerboundCustomPayloadPacket asServerBound()
	{
		return this.toC2SPacket();
	}

	@Override
	public ClientboundCustomPayloadPacket asClientBound()
	{
		return this.toS2CPacket();
	}

	@Override
	public void fromByteBuf(FriendlyByteBuf packet)
	{
		this.setServiceName(packet.readUtf());
		this.setServiceAddress(packet.readUtf());
		this.setServicePort(packet.readInt());
		this.setServiceUUID(packet.readUUID());
	}

	@Override
	public CoreServicePacket fromC2SPacket(ServerboundCustomPayloadPacket packet)
	{
		if (packet.payload() instanceof Payload)
		{
			return ((Payload) packet.payload()).data();
		}

		return null;
	}

	@Override
	public CoreServicePacket fromS2CPacket(ClientboundCustomPayloadPacket packet)
	{
		if (packet.payload() instanceof Payload)
		{
			return ((Payload) packet.payload()).data();
		}

		return null;
	}

	private static CoreServicePacket createFromByteBuf(FriendlyByteBuf buf)
	{
		return new CoreServicePacket(buf);
	}

	public record Payload(CoreServicePacket data)
			implements CustomPacketPayload, INetworkPayload<FriendlyByteBuf, Payload>
	{
		public static final Type<Payload> ID = new Type<>(PACKET_ID);
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec(Payload::write, Payload::new);
		public static final TypeAndCodec<FriendlyByteBuf, Payload> TYPE_AND_CODEC = new TypeAndCodec<>(ID, CODEC);

		public Payload(FriendlyByteBuf input)
		{
			this(CoreServicePacket.createFromByteBuf(input));
		}

		public void write(@NonNull FriendlyByteBuf buffer)
		{
			data.toByteBuf(buffer);
		}

		@Override
		public @NonNull Type<Payload> type()
		{
			return ID;
		}

		@Override
		public ResourceLocation getPacketId()
		{
			return ID.id();
		}

		@Override
		public TypeAndCodec<FriendlyByteBuf, Payload> getTypeAndCodec()
		{
			return TYPE_AND_CODEC;
		}
	}
}
