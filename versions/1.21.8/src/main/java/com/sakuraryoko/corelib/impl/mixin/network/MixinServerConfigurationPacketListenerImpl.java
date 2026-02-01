/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.mixin.network;

import com.llamalad7.mixinextras.sugar.Local;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.corelib.impl.events.players.PlayerEventsManager;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public class MixinServerConfigurationPacketListenerImpl
{
    @Shadow @Final private GameProfile gameProfile;

    @Inject(method = "handleConfigurationFinished",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V"))
    private void corelib$handleConfigurationFinished(ServerboundFinishConfigurationPacket serverboundFinishConfigurationPacket, CallbackInfo ci,
                                                          @Local ServerPlayer serverPlayer)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onCreatePlayer(serverPlayer, this.gameProfile);
    }
}
