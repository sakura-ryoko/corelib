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

package com.github.sakuraryoko.corelib.impl.events;

import com.github.sakuraryoko.corelib.api.config.ConfigHandler;
import com.github.sakuraryoko.corelib.api.events.IClientEventsDispatch;
import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.world.ClientWorld;

public class CoreClientEvents implements IClientEventsDispatch
{
    @Override
    public void worldChangePre(@Nullable ClientWorld world)
    {
        CoreLog.debug("worldChangePre: World Change Pre");
    }

    @Override
    public void worldChangePost(@Nullable ClientWorld world)
    {
        CoreLog.debug("worldChangePost: World Change Post");
    }

    @Override
    public void onJoining(@Nullable ClientWorld world)
    {
        CoreLog.debug("onJoining: joining");

        if (!CoreInitHandler.getInstance().isDedicatedServer())
        {
            ((ConfigHandler) ConfigHandler.getInstance()).loadAllConfigs();
        }
    }

    @Override
    public void onOpenConnection(@Nullable ClientWorld world)
    {
        CoreLog.debug("onOpenConnection: opening ClientConnection");
    }

    @Override
    public void onJoined(@Nullable ClientWorld world)
    {
        CoreLog.debug("onJoined: joined");

        ((ConfigHandler) ConfigHandler.getInstance()).saveAllConfigs();
    }

    @Override
    public void onGameJoinPre(@Nullable ClientWorld lastWorld)
    {
        CoreLog.debug("onGameJoinPre: onGameJoin Pre");
    }

    @Override
    public void onGameJoinPost(@Nullable ClientWorld newWorld)
    {
        CoreLog.debug("onGameJoinPost: onGameJoin Post");
    }

    @Override
    public void onDimensionChangePre(@Nullable ClientWorld world)
    {
        CoreLog.debug("onDimensionChangePre: dimension change pre");

        ((ConfigHandler) ConfigHandler.getInstance()).saveAllConfigs();
    }

    @Override
    public void onDimensionChangePost(@Nullable ClientWorld world)
    {
        CoreLog.debug("onDimensionChangePost: dimension change post");
    }

    @Override
    public void onDisconnecting(@Nullable ClientWorld world)
    {
        CoreLog.debug("onDisconnecting: disconnecting");
    }

    @Override
    public void onCloseConnection(@Nullable ClientWorld world)
    {
        CoreLog.debug("onCloseConnection: closing ClientConnection");
    }

    @Override
    public void onDisconnected(@Nullable ClientWorld world)
    {
        CoreLog.debug("onDisconnected: disconnected");

        if (!CoreInitHandler.getInstance().isDedicatedServer())
        {
            CoreLog.debug("onDisconnected: disconnected --> Not Dedicated");

            ((ConfigHandler) ConfigHandler.getInstance()).saveAllConfigs();
            ((ModInitHandler) ModInitHandler.getInstance()).reset();
        }
    }
}
