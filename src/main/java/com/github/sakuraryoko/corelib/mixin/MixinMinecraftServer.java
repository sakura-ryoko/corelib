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
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.WorldGenerationProgressListener;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer
{
    //@Shadow public abstract GameRules getGameRules();

    //@Shadow public abstract ServerWorld getOverworld();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z"), method = "runServer")
    private void corelib$onServerStarting(CallbackInfo ci)
    {
        ((ServerEventsHandler) ServerEventsHandler.getInstance()).onStarting((MinecraftServer) (Object) this);
    }

    //#if MC >= 11902
    //$$ @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;createMetadata()Lnet/minecraft/server/ServerMetadata;", ordinal = 0), method = "runServer")
    //#else
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V", ordinal = 0), method = "runServer")
    //#endif
    private void corelib$onServerStarted(CallbackInfo ci)
    {
        ((ServerEventsHandler) ServerEventsHandler.getInstance()).onStarted((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void corelib$onServerStopping(CallbackInfo info)
    {
        ((ServerEventsHandler) ServerEventsHandler.getInstance()).onStopping((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "shutdown")
    private void corelib$onServerStopped(CallbackInfo info)
    {
        ((ServerEventsHandler) ServerEventsHandler.getInstance()).onStopped((MinecraftServer) (Object) this);

        if (CoreInitHandler.getInstance().isDedicatedServer())
        {
            ((ModInitHandler) ModInitHandler.getInstance()).reset();
        }
    }

    /*
    @Inject(method = "prepareStartRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setSpawnPos(Lnet/minecraft/util/math/BlockPos;F)V", shift = At.Shift.AFTER))
    private void corelib$prepareStartRegion(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci)
    {
        //CoreLog.debug("MixinMinecraftServer#checkSpawnChunkRadius(): Spawn Position: "+this.getOverworld().getSpawnPos().toShortString()+" SPAWN_CHUNK_RADIUS: " + this.getGameRules().getInt(GameRules.SPAWN_CHUNK_RADIUS));
    }
     */
}
