package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerEventsHandler implements IPlayerEventsManager
{
    private static final PlayerEventsHandler INSTANCE = new PlayerEventsHandler();
    private final List<IPlayerEventsDispatch> handlers = new ArrayList<>();
    public static IPlayerEventsManager getInstance() { return INSTANCE; }

    @Override
    public void registerPlayerEvents(IPlayerEventsDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @Override
    public void unregisterPlayerEvents(IPlayerEventsDispatch handler)
    {
        this.handlers.remove(handler);
    }

    @ApiStatus.Internal
    public void onConnection(SocketAddress addr, GameProfile profile, @Nullable Text result)
    {
        if (result == null)
        {
            CoreLog.debug("onConnection: connection from {} // {}", addr.toString(), profile.getId().toString());
        }
        else
        {
            CoreLog.debug("onConnection: connection from {} // {} --> result: {}", addr.toString(), profile.getId().toString(), result.getLiteralString());
        }

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onConnection(addr, profile, result));
        }
    }

    @ApiStatus.Internal
    public void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection, ConnectedClientData clientData)
    {
        CoreLog.debug("onPlayerJoinPre: {} is joining.", player.getName().getLiteralString());

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerJoinPre(player, clientConnection, clientData));
        }
    }

    @ApiStatus.Internal
    public void onPlayerJoinPost(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerJoinPost: {} has joined.", player.getName().getLiteralString());

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerJoinPost(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerRespawn(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerRespawn: {} has respawned.", player.getName().getLiteralString());

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerRespawn(player));
        }
    }

    @ApiStatus.Internal
    public void onPlayerLeave(ServerPlayerEntity player)
    {
        CoreLog.debug("onPlayerLeave: {} has left.", player.getName().getLiteralString());

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onPlayerLeave(player));
        }
    }

    @ApiStatus.Internal
    public void onDisconnectAll()
    {
        CoreLog.debug("onDisconnectAll: disconnect all clients.");

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IPlayerEventsDispatch::onDisconnectAll);
        }
    }

    @ApiStatus.Internal
    public void onSetViewDistance(int distance)
    {
        CoreLog.debug("onSetViewDistance: setViewDistance to {}", distance);

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onSetViewDistance(distance));
        }
    }

    @ApiStatus.Internal
    public void onSetSimulationDistance(int distance)
    {
        CoreLog.debug("onSetSimulationDistance: setSimulationDistance to {}", distance);

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.onSetSimulationDistance(distance));
        }
    }
}
