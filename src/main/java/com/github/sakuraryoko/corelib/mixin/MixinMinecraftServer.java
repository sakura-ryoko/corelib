/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
