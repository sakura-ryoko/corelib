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

package com.sakuraryoko.corelib.impl.mixin;

//#if MC >= 11902
//$$ import net.minecraft.client.multiplayer.ClientLevel;
//#else
import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.corelib.impl.events.client.ClientEventsManager;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener
{
    //#if MC >= 11902
    //$$ @Shadow private ClientLevel level;
    //#else
    @Shadow private MultiPlayerLevel level;
    //#endif

    @Inject(method = "handleLogin", at = @At("HEAD"))
    private void corelib$onGameJoinPre(ClientboundLoginPacket packet, CallbackInfo ci)
    {
        ((ClientEventsManager) ClientEventsManager.getInstance()).onGameJoinPre(null);
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void corelib$onGameJoinPost(ClientboundLoginPacket packet, CallbackInfo ci)
    {
        ((ClientEventsManager) ClientEventsManager.getInstance()).onGameJoinPost(this.level);
    }
}
