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

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.events.server.ServerEventsManager;
import com.sakuraryoko.corelib.impl.modinit.ModInitManager;

@ApiStatus.Internal
@Mixin(IntegratedServer.class)
public class MixinIntegratedServer
{
    @Inject(method = "initServer", at = @At("RETURN"))
    private void corelib$onInitServer(CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitManager) ModInitManager.getInstance()).setIntegratedServer(true);
            ((ServerEventsManager) ServerEventsManager.getInstance()).onIntegratedStartedInternal((IntegratedServer) (Object) this);
        }
    }

    @Inject(method = "publishServer", at = @At("RETURN"))
    private void corelib$onPublishServer(GameType gameType, boolean bl, int i, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitManager) ModInitManager.getInstance()).setOpenToLan(true);
            ((ServerEventsManager) ServerEventsManager.getInstance()).onOpenToLanInternal((IntegratedServer) (Object) this, gameType);
        }
    }
}