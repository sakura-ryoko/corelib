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

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.corelib.api.network.packet.INetworkPacket;
import com.sakuraryoko.corelib.api.network.payload.INetworkPayload;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.mixin.network.c2s.IMixinServerboundCustomPayloadPacket_customPayload;

public class CoreServicePacket implements INetworkPacket<CoreServicePacket.C2SPayload, CoreServicePacket.S2CPayload>
{
	public static final ResourceLocation PACKET_ID = new ResourceLocation(Reference.MOD_ID, "network_service");
	private String serviceName;
	private String serviceAddress;
	private int servicePort;
	private UUID serviceUUID;

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
	public FriendlyByteBuf toByteBuf()
	{
		FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

		packet.writeUtf(this.serviceName);
		packet.writeUtf(this.serviceAddress);
		packet.writeInt(this.servicePort);
		packet.writeUUID(this.serviceUUID);

		return packet;
	}

	@Override
	public C2SPayload toC2SPacket()
	{
		return (C2SPayload) C2SPayload.fromPacket(this);
	}

	@Override
	public S2CPayload toS2CPacket()
	{
		return (S2CPayload) S2CPayload.fromPacket(this);
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
	public CoreServicePacket fromC2SPacket(C2SPayload packet)
	{
		return packet.toPacket();
	}

	@Override
	public CoreServicePacket fromS2CPacket(S2CPayload packet)
	{
		return packet.toPacket();
	}

	public static class C2SPayload extends ServerboundCustomPayloadPacket
			implements INetworkPayload<FriendlyByteBuf, CoreServicePacket.C2SPayload>
	{
		public C2SPayload(FriendlyByteBuf friendlyByteBuf)
		{
			super(PACKET_ID, friendlyByteBuf);
		}

		public C2SPayload()
		{
			this(new FriendlyByteBuf(Unpooled.buffer()));
		}

		public static ServerboundCustomPayloadPacket fromPacket(CoreServicePacket packet)
		{
			return new C2SPayload(packet.toByteBuf());
		}

		public CoreServicePacket toPacket()
		{
			return new CoreServicePacket(((IMixinServerboundCustomPayloadPacket_customPayload) this).corelib$getData());
		}

		@Override
		public ResourceLocation getPacketId()
		{
			return PACKET_ID;
		}

		@Override
		public C2SPayload fromByteBuf(FriendlyByteBuf buffer)
		{
			return new C2SPayload(buffer);
		}
	}

	public static class S2CPayload extends ClientboundCustomPayloadPacket
			implements INetworkPayload<FriendlyByteBuf, CoreServicePacket.S2CPayload>
	{
		public S2CPayload(FriendlyByteBuf friendlyByteBuf)
		{
			super(PACKET_ID, friendlyByteBuf);
		}

		public S2CPayload()
		{
			this(new FriendlyByteBuf(Unpooled.buffer()));
		}

		public static ClientboundCustomPayloadPacket fromPacket(CoreServicePacket packet)
		{
			return new S2CPayload(packet.toByteBuf());
		}

		public CoreServicePacket toPacket()
		{
			return new CoreServicePacket(this.getData());
		}

		@Override
		public ResourceLocation getPacketId()
		{
			return PACKET_ID;
		}

		@Override
		public S2CPayload fromByteBuf(FriendlyByteBuf buffer)
		{
			return new S2CPayload(buffer);
		}
	}
}
