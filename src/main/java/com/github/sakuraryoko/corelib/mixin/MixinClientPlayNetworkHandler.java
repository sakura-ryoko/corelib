package com.github.sakuraryoko.corelib.mixin;

import com.github.sakuraryoko.corelib.api.events.ClientEventsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler
{
    @Shadow private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void corelib$onGameJoinPre(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onGameJoinPre(null);
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void corelib$onGameJoinPost(GameJoinS2CPacket packet, CallbackInfo ci)
    {
        ((ClientEventsHandler) ClientEventsHandler.getInstance()).onGameJoinPost(this.world);
    }
}
