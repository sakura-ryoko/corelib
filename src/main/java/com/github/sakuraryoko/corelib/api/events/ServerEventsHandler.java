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
