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
//#if MC >= 12006
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

    //#if MC >= 12006
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

    //$$ @Inject(method = "respawnPlayer", at = @At("RETURN"))
    //$$ private void corelib$onPlayerRespawn(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir)
    //$$ {
        //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerRespawn(player);
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
    }

    //#if MC >= 11902
    //$$ @Inject(method = "setSimulationDistance", at = @At("HEAD"))
    //$$ private void corelib$onSetSimulationDistance(int viewDistance, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetSimulationDistance(viewDistance);
    //$$ }
    //#endif
}
