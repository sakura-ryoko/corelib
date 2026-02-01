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
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.corelib.impl.Reference;

public class CoreServicePacket
{
	public static final ResourceLocation PACKET_ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "network_service");
	private final String serviceName;
	private final String serviceAddress;
	private final int servicePort;
	private final UUID serviceUUID;

	public CoreServicePacket(String name, String address, int port, UUID uuid)
	{
		this.serviceName = name;
		this.serviceAddress = address;
		this.servicePort = port;
		this.serviceUUID = uuid;
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

	public void toByteBuf(FriendlyByteBuf packet)
	{
		packet.writeUtf(this.serviceName);
		packet.writeUtf(this.serviceAddress);
		packet.writeInt(this.servicePort);
		packet.writeUUID(this.serviceUUID);
	}

	public static CoreServicePacket fromByteBuf(FriendlyByteBuf packet)
	{
		String name = packet.readUtf();
		String address = packet.readUtf();
		int port = packet.readInt();
		UUID uuid = packet.readUUID();

		return new CoreServicePacket(name, address, port, uuid);
	}

	public record Payload(CoreServicePacket data) implements CustomPacketPayload
	{
		public static final Type<Payload> ID = new Type<>(PACKET_ID);
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = CustomPacketPayload.codec(Payload::write, Payload::new);

		public Payload(FriendlyByteBuf input)
		{
			this(CoreServicePacket.fromByteBuf(input));
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
	}
}
