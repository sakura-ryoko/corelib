package com.github.sakuraryoko.corelib.api.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;

public interface IServerEventsDispatch
{
    void onStarting(MinecraftServer server);

    void onStarted(MinecraftServer server);

    void onIntegratedStart(IntegratedServer server);

    void onOpenToLan(IntegratedServer server);

    void onDedicatedStart(DedicatedServer server);

    void onStopping(MinecraftServer server);

    void onStopped(MinecraftServer server);
}
