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
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
//#if MC >= 11902
//$$ import net.minecraft.client.multiplayer.ClientLevel;
//#else
import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
//#if MC >= 12006
//$$ import net.minecraft.client.gui.screens.ReceivingLevelScreen;
//#else
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.corelib.impl.config.ConfigManager;
import com.sakuraryoko.corelib.impl.events.client.ClientEventsManager;
import com.sakuraryoko.corelib.impl.modinit.CoreInit;
import com.sakuraryoko.corelib.impl.modinit.ModInitManager;

@ApiStatus.Internal
@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Shadow public abstract boolean isLocalServer();
    //#if MC >= 11902
    //$$ @Shadow @Nullable public ClientLevel level;
    //$$ @Unique ClientLevel lastLevel;
    //#else
    @Shadow @Nullable public MultiPlayerLevel level;
    @Unique MultiPlayerLevel lastLevel;
    //#endif

    @Inject(method = "<init>", at = @At("RETURN"))
    private void corelib$onGameInit(GameConfig gameConfig, CallbackInfo ci)
    {
        ModInitManager.getInstance().registerModInitHandler(new CoreInit());
        ((ModInitManager) ModInitManager.getInstance()).onModInit();
    }

    @Inject(method = "setLevel", at = @At("HEAD"))
    //#if MC >= 12006
    //$$ private void corelib$onWorldJoinPre(ClientLevel clientLevel, ReceivingLevelScreen.Reason reason, CallbackInfo ci)
    //#elseif MC >= 11902
    //$$ private void corelib$onWorldJoinPre(ClientLevel clientLevel, CallbackInfo ci)
    //#else
    private void corelib$onWorldJoinPre(MultiPlayerLevel clientLevel, CallbackInfo ci)
    //#endif
    {
        if (this.level == null)
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).onJoining(clientLevel);
            this.lastLevel = null;
        }
        else
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).onDimensionChangePre(clientLevel);
            this.lastLevel = clientLevel;
        }
    }

    @Inject(method = "setLevel", at = @At("RETURN"))
    //#if MC >= 12006
    //$$ private void corelib$onWorldJoinPost(ClientLevel clientLevel, ReceivingLevelScreen.Reason reason, CallbackInfo ci)
    //#elseif MC >= 11902
    //$$ private void corelib$onWorldJoinPost(ClientLevel clientLevel, CallbackInfo ci)
    //#else
    private void corelib$onWorldJoinPost(MultiPlayerLevel clientLevel, CallbackInfo ci)
    //#endif
    {
        if (this.lastLevel != null)
        {
            ConfigManager.getInstance().reloadAllConfigs();
            ((ClientEventsManager) ClientEventsManager.getInstance()).onDimensionChangePost(clientLevel);
        }
        else
        {
            if (this.isLocalServer())
            {
                ((ClientEventsManager) ClientEventsManager.getInstance()).onOpenConnection(clientLevel);
            }

            ((ClientEventsManager) ClientEventsManager.getInstance()).onJoined(clientLevel);
        }

        this.lastLevel = clientLevel;
    }

    //#if MC >= 12006
    //$$ @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V", at = @At("HEAD"))
    //$$ private void corelib$onDisconnectPre(Screen screen, boolean transferring, CallbackInfo ci)
    //#else
    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    private void corelib$onDisconnectPre(Screen screen, CallbackInfo ci)
    //#endif
    {
        if (this.lastLevel == null)
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).worldChangePre(this.level);
        }
        else
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).onDisconnecting(this.level);
        }
    }

    //#if MC >= 12006
    //$$ @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V", at = @At("RETURN"))
    //$$ private void corelib$onDisconnectPost(Screen screen, boolean transferring, CallbackInfo ci)
    //#else
    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("RETURN"))
    private void corelib$onDisconnectPost(Screen screen, CallbackInfo ci)
    //#endif
    {
        if (this.lastLevel != null)
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).onDisconnected(this.lastLevel);
            //ConfigManager.getInstance().saveAllConfigs();
            this.lastLevel = null;
        }
        else
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).worldChangePost(null);
        }
    }

    //#if MC >= 12006
    //$$ @Inject(method = "clearDownloadedResourcePacks", at = @At("HEAD"))
    //#elseif MC >= 11903
    //$$ @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            //$$ at = @At(value = "INVOKE",
                     //$$ target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;close()V"))

    //#else
    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;cleanup()V"))
    //#endif
    private void corelib$onDisconnected(CallbackInfo ci)
    {
        if (this.isLocalServer())
        {
            ((ClientEventsManager) ClientEventsManager.getInstance()).onCloseConnection(null);
        }

        ConfigManager.getInstance().saveAllConfigs();
        ((ModInitManager) ModInitManager.getInstance()).reset();
    }
}
