package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import net.minecraft.client.world.ClientWorld;

public interface IClientEventsDispatch
{
    void worldChangePre(@Nullable ClientWorld world);

    void worldChangePost(@Nullable ClientWorld world);

    void onJoining(@Nullable ClientWorld world);

    void onOpenConnection(@Nullable ClientWorld world);

    void onJoined(@Nullable ClientWorld world);

    void onGameJoinPre(@Nullable ClientWorld lastWorld);

    void onGameJoinPost(@Nullable ClientWorld newWorld);

    void onDimensionChangePre(@Nullable ClientWorld world);

    void onDimensionChangePost(@Nullable ClientWorld world);

    void onDisconnecting(@Nullable ClientWorld world);

    void onCloseConnection(@Nullable ClientWorld world);

    void onDisconnected(@Nullable ClientWorld world);
}
