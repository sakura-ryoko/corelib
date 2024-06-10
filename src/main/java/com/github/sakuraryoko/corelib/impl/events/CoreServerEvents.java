package com.github.sakuraryoko.corelib.impl.events;

import com.github.sakuraryoko.corelib.api.config.ConfigHandler;
import com.github.sakuraryoko.corelib.api.events.IServerEventsDispatch;
import com.github.sakuraryoko.corelib.impl.commands.TestCommand;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.impl.text.NodeManagerV1;
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
        NodeManagerV1.registerNodes();
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
