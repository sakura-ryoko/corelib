/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * CoreLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoreLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CoreLib.  If not, see <https://www.gnu.org/licenses/>.
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
