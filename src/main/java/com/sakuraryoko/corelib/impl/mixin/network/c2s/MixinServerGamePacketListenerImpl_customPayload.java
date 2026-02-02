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

import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.network.PacketListenerManager;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl_customPayload
{
	@Shadow public ServerPlayer player;

	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	private void corelib$handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci)
	{
		if (!Reference.EXPERIMENTAL) return;
		ResourceLocation id = ((IMixinServerboundCustomPayloadPacket_customPayload) packet).corelib$getIdentifier();

		if (PacketListenerManager.getInstance().onC2SPacketReceived(id, packet, this.player))
		{
			ci.cancel();
		}
	}
}
