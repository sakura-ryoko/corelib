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

package com.sakuraryoko.corelib.impl.mixin.network.payload;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.network.announcer.CoreServiceHandler;
import com.sakuraryoko.corelib.impl.network.announcer.CoreServicePacket;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MixinServerCommonPacketListenerImpl
{
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	private void corelib$handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci)
	{
		if (!Reference.EXPERIMENTAL) return;
		if ((ServerCommonPacketListenerImpl) (Object) this instanceof ServerGamePacketListenerImpl)
		{
			if (packet.payload().id().equals(CoreServicePacket.PACKET_ID))
			{
				ServerGamePacketListenerImpl handler = (ServerGamePacketListenerImpl) (Object) this;

				CoreServiceHandler.getInstance().getServerHandler().receivePacket(packet, handler.player);
				ci.cancel();
			}
		}
	}
}
