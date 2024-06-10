package com.github.sakuraryoko.corelib.mixin;

import com.github.sakuraryoko.corelib.api.events.ServerEventsHandler;
import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameMode;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer
{
    @Inject(method = "setupServer", at = @At("RETURN"))
    private void corelib$setupServer(CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitHandler) ModInitHandler.getInstance()).setIntegratedServer(true);
            ((ServerEventsHandler) ServerEventsHandler.getInstance()).onIntegratedStart((IntegratedServer) (Object) this);
        }
    }

    @Inject(method = "openToLan", at = @At("RETURN"))
    private void corelib$checkOpenToLan(GameMode gameMode, boolean cheatsAllowed, int port, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue())
        {
            ((ModInitHandler) ModInitHandler.getInstance()).setOpenToLan(true);
            ((ServerEventsHandler) ServerEventsHandler.getInstance()).onOpenToLan((IntegratedServer) (Object) this);
        }
    }
}
