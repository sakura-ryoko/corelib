package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import java.net.SocketAddress;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface IPlayerEventsDispatch
{
    void onConnection(SocketAddress addr, GameProfile profile, @Nullable Text result);

    void onPlayerJoinPre(ServerPlayerEntity player, ClientConnection clientConnection, ConnectedClientData clientData);

    void onPlayerJoinPost(ServerPlayerEntity player);

    void onPlayerRespawn(ServerPlayerEntity newPlayer);

    void onPlayerLeave(ServerPlayerEntity player);

    void onDisconnectAll();

    void onSetViewDistance(int distance);

    void onSetSimulationDistance(int distance);
}
