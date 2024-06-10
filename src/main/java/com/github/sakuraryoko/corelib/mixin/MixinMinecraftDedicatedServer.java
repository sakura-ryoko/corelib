package com.github.sakuraryoko.corelib.mixin;

import com.github.sakuraryoko.corelib.api.events.ServerEventsHandler;
import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerPropertiesLoader;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(MinecraftDedicatedServer.class)
public class MixinMinecraftDedicatedServer
{
    @Inject(method = "<init>", at = @At("RETURN"))
    private void corelib$onDedicatedServer(Thread serverThread, LevelStorage.Session session,
                                           ResourcePackManager dataPackManager, SaveLoader saveLoader,
                                           ServerPropertiesLoader propertiesLoader, DataFixer dataFixer,
                                           ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci)
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
