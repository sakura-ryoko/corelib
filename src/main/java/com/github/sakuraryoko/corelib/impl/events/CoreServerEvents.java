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
import com.github.sakuraryoko.corelib.api.events.IServerEventsDispatch;
import com.github.sakuraryoko.corelib.impl.commands.TestCommand;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.impl.text.NodeManager;
import com.github.sakuraryoko.corelib.util.CoreLog;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.integrated.IntegratedServer;

public class CoreServerEvents implements IServerEventsDispatch
{
    @Override
    public void onStarting(MinecraftServer server)
    {
        CoreLog.debug("onStarting: server starting");

        if (CoreInitHandler.getInstance().isDedicatedServer())
        {
            ((ConfigHandler) ConfigHandler.getInstance()).loadAllConfigs();
        }
        NodeManager.registerNodes();
        TestCommand.register();
    }

    @Override
    public void onStarted(MinecraftServer server)
    {
        CoreLog.debug("onStarted: server started");

        if (CoreInitHandler.getInstance().isDedicatedServer())
        {
            ((ConfigHandler) ConfigHandler.getInstance()).saveAllConfigs();
        }
    }

    @Override
    public void onIntegratedStart(IntegratedServer server)
    {
        CoreLog.debug("onIntegratedStart: integrated server setup");
    }

    @Override
    public void onOpenToLan(IntegratedServer server)
    {
        CoreLog.debug("onOpenToLan: open to lan started");
    }

    @Override
    public void onDedicatedStart(DedicatedServer server)
    {
        CoreLog.debug("onDedicatedStart: dedicated server setup");
    }

    @Override
    public void onStopping(MinecraftServer server)
    {
        CoreLog.debug("onStopping: server stopping");
    }

    @Override
    public void onStopped(MinecraftServer server)
    {
        CoreLog.debug("onStopped: server stopped");

        if (CoreInitHandler.getInstance().isDedicatedServer())
        {
            ((ConfigHandler) ConfigHandler.getInstance()).saveAllConfigs();
        }
    }
}
