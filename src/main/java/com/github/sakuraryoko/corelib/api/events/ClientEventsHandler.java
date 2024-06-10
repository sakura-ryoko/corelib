package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class ClientEventsHandler implements IClientEventsManager
{
    private static final ClientEventsHandler INSTANCE = new ClientEventsHandler();
    private final List<IClientEventsDispatch> handlers = new ArrayList<>();
    public static IClientEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerClientEvents(IClientEventsDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterClientEvents(IClientEventsDispatch handler)
    {
        this.handlers.remove(handler);
    }

    @ApiStatus.Internal
    public void worldChangePre(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.worldChangePre(world));
        }
    }

    @ApiStatus.Internal
    public void worldChangePost(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.worldChangePost(world));
        }
    }

    @ApiStatus.Internal
    public void onJoining(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onJoining(world));
        }
    }

    @ApiStatus.Internal
    public void onOpenConnection(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onOpenConnection(world));
        }
    }

    @ApiStatus.Internal
    public void onJoined(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onJoined(world));
        }
    }

    @ApiStatus.Internal
    public void onGameJoinPre(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onGameJoinPre(world));
        }
    }

    @ApiStatus.Internal
    public void onGameJoinPost(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onGameJoinPost(world));
        }
    }

    @ApiStatus.Internal
    public void onDimensionChangePre(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDimensionChangePre(world));
        }
    }

    @ApiStatus.Internal
    public void onDimensionChangePost(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDimensionChangePost(world));
        }
    }

    @ApiStatus.Internal
    public void onDisconnecting(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDisconnecting(world));
        }
    }

    @ApiStatus.Internal
    public void onCloseConnection(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onCloseConnection(world));
        }
    }

    @ApiStatus.Internal
    public void onDisconnected(@Nullable ClientWorld world)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onDisconnected(world));
        }
    }
}
