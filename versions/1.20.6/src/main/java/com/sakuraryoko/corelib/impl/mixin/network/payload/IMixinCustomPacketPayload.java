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

import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.network.announcer.CachedPayloadList;
import com.sakuraryoko.corelib.impl.network.announcer.CoreServicePacket;

/**
 * Copied from Carpet Mod.
 */
@Mixin(CustomPacketPayload.class)
public interface IMixinCustomPacketPayload
{
	@Inject(method = "codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;",
	        at = @At("HEAD"), cancellable = true)
	private static <B extends FriendlyByteBuf> void corelib$onTaintedCodec(CustomPacketPayload.FallbackProvider<B> fallbackProvider,
	                                                                       List<CustomPacketPayload.TypeAndCodec<? super B, ?>> typeAndCodecs,
	                                                                       CallbackInfoReturnable<StreamCodec<B, CustomPacketPayload>> cir)
	{
		if (!Reference.EXPERIMENTAL) return;
		if (!(typeAndCodecs instanceof CachedPayloadList))
		{
			List<CustomPacketPayload.TypeAndCodec<? super B, ?>> fixedList = new CachedPayloadList<>(typeAndCodecs);

			fixedList.add(new CustomPacketPayload.TypeAndCodec<>(CoreServicePacket.Payload.ID, CoreServicePacket.Payload.CODEC));

			cir.setReturnValue(CustomPacketPayload.codec(fallbackProvider, fixedList));
		}
	}
}
