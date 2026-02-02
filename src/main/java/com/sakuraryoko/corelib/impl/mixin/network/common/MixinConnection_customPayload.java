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

package com.sakuraryoko.corelib.impl.mixin.network.common;

import java.util.Set;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.mixin.network.c2s.IMixinServerboundCustomPayloadPacket_customPayload;
import com.sakuraryoko.corelib.impl.network.PacketListenerManager;

@Mixin(Connection.class)
public class MixinConnection_customPayload
{
	@Shadow private Channel channel;
	@Shadow @Final public static AttributeKey<ConnectionProtocol> ATTRIBUTE_PROTOCOL;

	@Redirect(method = "sendPacket",
	          at = @At(value = "INVOKE",
	                 target = "Lnet/minecraft/network/ConnectionProtocol;getProtocolForPacket(Lnet/minecraft/network/protocol/Packet;)Lnet/minecraft/network/ConnectionProtocol;"))
	private ConnectionProtocol corelib$onSendPacketFix(Packet<?> packet)
	{
		ConnectionProtocol protocol = ConnectionProtocol.getProtocolForPacket(packet);

		if (Reference.EXPERIMENTAL && protocol == null)
		{
			ConnectionProtocol fixedProtocol = this.channel.attr(ATTRIBUTE_PROTOCOL).get();

			if (packet instanceof ClientboundCustomPayloadPacket)
			{
				ResourceLocation id = ((ClientboundCustomPayloadPacket) packet).getIdentifier();
				Set<ResourceLocation> ids = PacketListenerManager.getInstance().getS2CKeySet();

				if (ids.contains(id))
				{
					return fixedProtocol;
				}
			}
			else if (packet instanceof ServerboundCustomPayloadPacket)
			{
				ResourceLocation id = ((IMixinServerboundCustomPayloadPacket_customPayload) packet).corelib$getIdentifier();
				Set<ResourceLocation> ids = PacketListenerManager.getInstance().getC2SKeySet();

				if (ids.contains(id))
				{
					return fixedProtocol;
				}
			}
		}

		return protocol;
	}
}
