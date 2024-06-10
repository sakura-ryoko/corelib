/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
