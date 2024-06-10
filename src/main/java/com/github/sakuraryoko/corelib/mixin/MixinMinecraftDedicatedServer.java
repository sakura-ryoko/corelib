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

import com.github.sakuraryoko.corelib.api.events.ServerEventsHandler;
import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
//#if MC >= 11902
//$$ import net.minecraft.server.SaveLoader;
//#endif
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesLoader;
//#if MC >= 11902
//$$ import net.minecraft.util.ApiServices;
//#endif
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(MinecraftDedicatedServer.class)
public class MixinMinecraftDedicatedServer
{
    @Inject(method = "<init>", at = @At("RETURN"))
    //#if MC >= 11902
    //$$ private void corelib$onDedicatedServer(Thread serverThread, LevelStorage.Session session,
                                           //$$ ResourcePackManager dataPackManager, SaveLoader saveLoader,
                                           //$$ ServerPropertiesLoader propertiesLoader, DataFixer dataFixer,
                                           //$$ ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci)
    //#else
    private void corelib$onDedicatedServer(Thread serverThread, DynamicRegistryManager.Impl registryManager, LevelStorage.Session session,
                                           ResourcePackManager dataPackManager, ServerResourceManager serverResourceManager, SaveProperties saveProperties,
                                           ServerPropertiesLoader propertiesLoader, DataFixer dataFixer, MinecraftSessionService sessionService,
                                           GameProfileRepository gameProfileRepo, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci)
    //#endif
    {
        ((ModInitHandler) ModInitHandler.getInstance()).onModInit();
    }

    @Inject(method = "setupServer", at = @At("RETURN"))
    private void corelib$setupServer(CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitHandler) ModInitHandler.getInstance()).setDedicatedServer(true);
            ((ServerEventsHandler) ServerEventsHandler.getInstance()).onDedicatedStart((DedicatedServer) this);
        }
    }
}
