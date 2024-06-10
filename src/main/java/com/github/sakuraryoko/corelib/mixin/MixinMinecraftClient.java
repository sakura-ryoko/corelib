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
import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
//#if MC >= 12006
//$$ import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
//#endif
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient
{
    @Shadow @Nullable public abstract IntegratedServer getServer();

    @Shadow @Nullable public ClientWorld world;

    @Unique
    private ClientWorld lastWorld;

    @Shadow private boolean integratedServerRunning;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void corelib$onGameInit(RunArgs args, CallbackInfo ci)
    {
        ((ModInitHandler) ModInitHandler.getInstance()).onModInit();
    }

    @Inject(method ="joinWorld", at = @At("HEAD"))
    //#if MC >= 12006
    //$$ private void corelib$onJoinWorldPre(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci)
    //#else
    private void corelib$onJoinWorldPre(ClientWorld world, CallbackInfo ci)
    //#endif
    {
        if (this.world == null)
        {
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onJoining(world);
            this.lastWorld = null;
        }
        else
        {
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onDimensionChangePre(world);
            this.lastWorld = world;
        }
    }

    @Inject(method ="joinWorld", at = @At("TAIL"))
    //#if MC >= 12006
    //$$ private void corelib$onJoinWorldPost(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci)
    //#else
    private void corelib$onJoinWorldPost(ClientWorld world, CallbackInfo ci)
    //#endif
    {
        if (this.lastWorld != null)
        {
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onDimensionChangePost(world);
        }
        else
        {
            if (!this.integratedServerRunning)
            {
                ((ClientEventsHandler) ClientEventsHandler.getInstance()).onOpenConnection(world);
            }

            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onJoined(world);
        }

        this.lastWorld = world;
    }

    //#if MC >= 12006
    //$$ @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("HEAD"))
    //#else
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    //#endif
    private void corelib$disconnectPre(CallbackInfo ci)
    {
        if (this.lastWorld == null)
        {
            // Called before <init> a new world
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).worldChangePre(world);
        }
        else
        {
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onDisconnecting(world);
            this.lastWorld = this.world;
        }
    }

    //#if MC >= 12006
    //$$ @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("RETURN"))
    //$$ private void corelib$disconnectPost(Screen disconnectionScreen, boolean transferring, CallbackInfo ci)
    //#else
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    private void corelib$disconnectPost(Screen screen, CallbackInfo ci)
    //#endif
    {
        if (this.lastWorld != null)
        {
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).onDisconnected(this.lastWorld);
            this.lastWorld = null;
        }
        else
        {
            // Called before <init> a new world
            ((ClientEventsHandler) ClientEventsHandler.getInstance()).worldChangePost(null);
        }
    }

    //#if MC >= 12006
    //$$ @Inject(method = "onDisconnected", at = @At("HEAD"))
    //#else
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;clearWorld()V"))
    //#endif
    private void corelib$onDisconnected(CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onCloseConnection(null);
    }
}
