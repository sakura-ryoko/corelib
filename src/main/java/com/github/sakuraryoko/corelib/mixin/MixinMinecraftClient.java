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
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
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
    private void corelib$onJoinWorldPre(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci)
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
    private void corelib$onJoinWorldPost(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci)
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

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("HEAD"))
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

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At("RETURN"))
    private void corelib$disconnectPost(Screen disconnectionScreen, boolean transferring, CallbackInfo ci)
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

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void corelib$onDisconnected(CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onCloseConnection(null);
    }
}
