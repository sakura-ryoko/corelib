/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
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

package com.github.sakuraryoko.corelib.mixin;

import com.github.sakuraryoko.corelib.api.events.ClientEventsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler
{
    @Shadow private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void corelib$onGameJoinPre(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onGameJoinPre(null);
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void corelib$onGameJoinPost(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onGameJoinPost(this.world);
    }
}
