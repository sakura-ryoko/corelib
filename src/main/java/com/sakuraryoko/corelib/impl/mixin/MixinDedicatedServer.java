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

import java.io.File;
import org.jetbrains.annotations.ApiStatus;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
//#if MC >= 11902
//$$ import net.minecraft.server.Services;
//$$ import net.minecraft.server.WorldStem;
//$$ import net.minecraft.server.packs.repository.PackRepository;
//$$ import net.minecraft.world.level.storage.LevelStorageSource;
//#else
//#endif
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.GameProfileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.config.ConfigManager;
import com.sakuraryoko.corelib.impl.events.server.ServerEventsManager;
import com.sakuraryoko.corelib.impl.modinit.ModInitManager;

@ApiStatus.Internal
@Mixin(DedicatedServer.class)
public class MixinDedicatedServer
{
    @Inject(method = "<init>", at = @At("RETURN"))
//#if MC >= 11902
    //$$ private void corelib$onDedicatedServer(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess,
                                              //$$ PackRepository packRepository, WorldStem worldStem,
                                              //$$ DedicatedServerSettings dedicatedServerSettings, DataFixer dataFixer,
                                              //$$ Services services, ChunkProgressListenerFactory chunkProgressListenerFactory,
                                              //$$ CallbackInfo ci)
//#else
private void corelib$onDedicatedServer(File file, DedicatedServerSettings dedicatedServerSettings, DataFixer dataFixer,
                                       YggdrasilAuthenticationService yggdrasilAuthenticationService,
                                       MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository,
                                       GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory,
                                       String string, CallbackInfo ci)
//#endif
    {
        ((ModInitManager) ModInitManager.getInstance()).onModInit();
    }


    @Inject(method = "initServer", at = @At("RETURN"))
    private void corelib$onInitServer(CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitManager) ModInitManager.getInstance()).setDedicatedServer(true);
            ((ServerEventsManager) ServerEventsManager.getInstance()).onDedicatedStartedInternal(((DedicatedServer) (Object) this));
        }
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void corelib$onStopServer(CallbackInfo ci)
    {
        ((ServerEventsManager) ServerEventsManager.getInstance()).onDedicatedStoppingInternal(((DedicatedServer) (Object) this));
        ConfigManager.getInstance().saveAllConfigs();
    }
}