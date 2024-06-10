package com.github.sakuraryoko.corelib.api.events;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;

public class ServerEventsHandler implements IServerEventsManager
{
    private static final ServerEventsHandler INSTANCE = new ServerEventsHandler();
    private final List<IServerEventsDispatch> handlers = new ArrayList<>();
    public static IServerEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerServerEvents(IServerEventsDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterServerEvents(IServerEventsDispatch handler)
    {
        this.handlers.remove(handler);
    }

    @ApiStatus.Internal
    public void onStarting(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStarting(server));
        }
    }

    @ApiStatus.Internal
    public void onStarted(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStarted(server));
        }
    }

    @ApiStatus.Internal
    public void onIntegratedStart(IntegratedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onIntegratedStart(server));
        }
    }

    @ApiStatus.Internal
    public void onOpenToLan(IntegratedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onOpenToLan(server));
        }
    }

    @ApiStatus.Internal
    public void onDedicatedStart(DedicatedServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDedicatedStart(server));
        }
    }

    @ApiStatus.Internal
    public void onStopping(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStopping(server));
        }
    }

    @ApiStatus.Internal
    public void onStopped(MinecraftServer server)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onStopped(server));
        }
    }
}
