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

import java.net.SocketAddress;
import com.github.sakuraryoko.corelib.api.events.PlayerEventsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.GameProfile;
//#if MC >= 11902
//$$ import net.minecraft.entity.Entity;
//#endif
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
//#if MC >= 12002
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager
{
    public MixinPlayerManager() { super(); }

    @Inject(method = "checkCanJoin", at = @At("RETURN"))
    private void corelib$onCheckCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onConnection(address, profile, cir.getReturnValue());
    }

    //#if MC >= 12002
    //$$ @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    //$$ private void corelib$onPlayerJoinPre(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPre(player, connection, clientData);
    //$$ }

    //$$ @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    //$$ private void corelib$onPlayerJoinPost(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPost(player);
    //$$ }

    //#else
    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    private void corelib$onPlayerJoinPre(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPre(player, connection);
    }

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void corelib$onPlayerJoinPost(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPost(player);
    }
    //#endif

    //#if MC >= 12100
    //$$ @Inject(method = "respawnPlayer", at = @At("RETURN"))
    //$$ private void corelib$onPlayerRespawn(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir)
    //$$ {
    //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerRespawn(player);
    //$$ }
    //#else
    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void corelib$onPlayerRespawn(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerRespawn(player);
    }
    //#endif

    @Inject(method = "remove", at = @At("HEAD"))
    private void corelib$onPlayerLeave(ServerPlayerEntity player, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerLeave(player);
    }

    @Inject(method = "disconnectAllPlayers", at = @At("HEAD"))
    private void corelib$onDisconnectAll(CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onDisconnectAll();
    }

    @Inject(method = "setViewDistance", at = @At("HEAD"))
    private void corelib$onSetViewDistance(int viewDistance, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetViewDistance(viewDistance);
        // Under 1.17, simulationDistance and viewDistance is basically the same (aka watchDistance)
        //#if MC >= 11904
        //#else
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetSimulationDistance(viewDistance);
        //#endif
    }

    //#if MC >= 11904
    //$$ @Inject(method = "setSimulationDistance", at = @At("HEAD"))
    //$$ private void corelib$onSetSimulationDistance(int viewDistance, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetSimulationDistance(viewDistance);
    //$$ }
    //#endif
}
