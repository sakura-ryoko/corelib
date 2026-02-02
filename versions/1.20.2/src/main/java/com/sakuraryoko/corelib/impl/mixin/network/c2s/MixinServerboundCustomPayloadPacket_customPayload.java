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

package com.sakuraryoko.corelib.impl.mixin.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.network.PayloadManager;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket_customPayload
{
	@Inject(method = "readPayload",
	        at = @At(value = "INVOKE",
	                 target = "Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;readUnknownPayload(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;"), cancellable = true)
	private static <P extends CustomPacketPayload> void corelib$onReadPayloadC2S(ResourceLocation id, FriendlyByteBuf buffer, CallbackInfoReturnable<CustomPacketPayload> cir)
	{
		if (Reference.EXPERIMENTAL)
		{
			P payload = PayloadManager.getInstance().fromByteBuf(id, buffer);

			if (payload != null)
			{
				cir.setReturnValue(payload);
			}
		}
	}
}
