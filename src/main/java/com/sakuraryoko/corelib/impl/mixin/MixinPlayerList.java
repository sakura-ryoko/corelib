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

import java.net.SocketAddress;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
//#if MC >= 12002
//$$ import net.minecraft.server.level.ClientInformation;
//$$ import net.minecraft.server.network.CommonListenerCookie;
//#else
//#endif
import net.minecraft.server.players.PlayerList;
//#if MC >= 12101
//$$ import net.minecraft.world.entity.Entity;
//#elseif MC >= 11902
//$$ import net.minecraft.world.entity.player.ProfilePublicKey;
//#else
import net.minecraft.world.level.dimension.DimensionType;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.events.players.PlayerEventsManager;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList
{
    public MixinPlayerList()
    {
        super();
    }

    @Inject(method = "canPlayerLogin", at = @At("RETURN"))
    private void corelib$canPlayerLogin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Component> cir)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onConnection(address, profile, cir.getReturnValue());
    }

    @Inject(method = "getPlayerForLogin", at = @At("RETURN"))
    //#if MC >= 12002
    //$$ private void corelib$onGetPlayerForLogin(GameProfile gameProfile, ClientInformation clientInformation, CallbackInfoReturnable<ServerPlayer> cir)
    //#elseif MC >= 11903
    //$$ private void corelib$onGetPlayerForLogin(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir)
    //#elseif MC >= 11902
    //$$ private void corelib$onGetPlayerForLogin(GameProfile gameProfile, ProfilePublicKey profilePublicKey, CallbackInfoReturnable<ServerPlayer> cir)
    //#else
    private void corelib$onGetPlayerForLogin(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir)
    //#endif
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onCreatePlayer(cir.getReturnValue(), gameProfile);
    }

    // onPlayerConnect
    //#if MC >= 12002
    //$$ @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    //$$ private void corelib$onPlaceNewPlayerPre(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerJoinPre(serverPlayer, connection);
    //$$ }

    //$$ @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    //$$ private void corelib$onPlaceNewPlayerPost(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerJoinPost(serverPlayer, connection);
    //$$ }

    //#else
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void corelib$onPlaceNewPlayerPre(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerJoinPre(serverPlayer, connection);
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void corelib$onPlaceNewPlayerPost(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerJoinPost(serverPlayer, connection);
    }
    //#endif

    // respawnPlayer
    @Inject(method = "respawn", at = @At("RETURN"))
    //#if MC >= 12101
    //$$ private void corelib$onRespawn(ServerPlayer serverPlayer, boolean bl, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir)
    //#elseif MC >= 11902
    //$$ private void corelib$onRespawn(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir)
    //#elseif MC >= 11605
    //$$ private void corelib$onRespawn(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir)
    //#else
    private void corelib$onRespawn(ServerPlayer serverPlayer, DimensionType dimensionType, boolean bl, CallbackInfoReturnable<ServerPlayer> cir)
    //#endif
    {
        // serverPlayer = oldObject
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerRespawn(cir.getReturnValue());
    }

    // remove
    @Inject(method = "remove", at = @At("HEAD"))
    private void corelib$onRemove(ServerPlayer serverPlayer, CallbackInfo ci)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onPlayerLeave(serverPlayer);
    }

    // disconnectAllPlayers
    @Inject(method = "removeAll", at = @At("HEAD"))
    private void corelib$onRemoveAll(CallbackInfo ci)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onDisconnectAll();
    }

    @Inject(method = "setViewDistance", at = @At("HEAD"))
    private void corelib$onSetViewDistance(int viewDistance, CallbackInfo ci)
    {
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onSetViewDistance(viewDistance);
        // Under 1.17, simulationDistance and viewDistance is basically the same (aka watchDistance)
        //#if MC >= 11904
        //#else
        ((PlayerEventsManager) PlayerEventsManager.getInstance()).onSetSimulationDistance(viewDistance);
        //#endif
    }

    //#if MC >= 11904
    //$$ @Inject(method = "setSimulationDistance", at = @At("HEAD"))
    //$$ private void corelib$onSetSimulationDistance(int viewDistance, CallbackInfo ci)
    //$$ {
        //$$ ((PlayerEventsManager) PlayerEventsManager.getInstance()).onSetSimulationDistance(viewDistance);
    //$$ }
    //#endif
}
