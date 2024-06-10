package com.github.sakuraryoko.corelib.mixin;

import java.net.SocketAddress;
import com.github.sakuraryoko.corelib.api.events.PlayerEventsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
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

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    private void corelib$onPlayerJoinPre(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPre(player, connection, clientData);
    }

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void corelib$onPlayerJoinPost(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerJoinPost(player);
    }

    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    private void corelib$onPlayerRespawn(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onPlayerRespawn(player);
    }

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

    @Inject(method ="setViewDistance", at = @At("HEAD"))
    private void corelib$onSetViewDistance(int viewDistance, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetViewDistance(viewDistance);
    }

    @Inject(method ="setSimulationDistance", at = @At("HEAD"))
    private void corelib$onSetSimulationDistance(int viewDistance, CallbackInfo ci)
    {
        ((PlayerEventsHandler) PlayerEventsHandler.getInstance()).onSetSimulationDistance(viewDistance);
    }
}
