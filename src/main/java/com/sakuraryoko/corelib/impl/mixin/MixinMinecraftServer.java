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

//#if MC >= 11605
//$$ import java.util.Collection;
//$$ import java.util.concurrent.CompletableFuture;
//#else
//#endif
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC >= 11605
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#else
//#endif

import com.sakuraryoko.corelib.impl.events.server.ServerEventsManager;

@ApiStatus.Internal
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer
{
    //#if MC >= 11605
    //#else
    @Shadow public abstract ReloadableResourceManager getResources();
    //#endif

    //#if MC >= 11605
    //$$ @Inject(method = "runServer",
            //$$ at = @At(value = "INVOKE",
                     //$$ target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
    //#else
    @Inject(method = "run",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
    //#endif
    private void corelib$onServerStarting(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onStartingInternal((MinecraftServer) (Object) this);
    }

    //#if MC >= 11605
    //$$ @Inject(method = "runServer",
    //#else
    @Inject(method = "run",
    //#endif
            at = @At(value = "INVOKE",
                     //#if MC >= 11904
                     //$$ target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;",
                     //#else
                     target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V",
                     //#endif
                     ordinal = 0))
    private void corelib$onServerStarted(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onStartedInternal((MinecraftServer) (Object) this);
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    //#if MC >= 11605
    //$$ private void corelib$onReloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir)
    //$${
        //$$((ServerEventsManager) ServerEventsManager.getInstance()).onReloadCompleteInternal((MinecraftServer) (Object) this, collection);
    //$$}
    //#else
    private void corelib$onReloadResources(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onReloadCompleteInternal((MinecraftServer) (Object) this, this.getResources().getNamespaces());
    }
    //#endif

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void corelib$onStoppingServer(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onStoppingInternal((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("TAIL"))
    private void corelib$onStoppedServer(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onStoppedInternal((MinecraftServer) (Object) this);
    }
}
