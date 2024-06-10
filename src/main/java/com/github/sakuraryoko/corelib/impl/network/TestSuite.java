package com.github.sakuraryoko.corelib.impl.network;

import java.util.Iterator;
import java.util.Set;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.util.CoreLog;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TestSuite
{
    public static void testS2C(ServerPlayerEntity player, String msg)
    {
        if (CoreInitHandler.getInstance().isServer())
        {
        }
        else
            CoreLog.info("testS2C() called from a Client Environment.");
    }

    public static void testC2S(String msg)
    {
        if (CoreInitHandler.getInstance().isClient())
        {
        }
        else
            CoreLog.info("testC2S() called from a Server Environment.");
    }

    public static void checkClientChannels()
    {
        CoreLog.debug("DebugSuite#checkGlobalChannels(): Start.");
        Set<Identifier> channels = ClientPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;

        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            CoreLog.debug("DebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        CoreLog.debug("DebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    }

    public static void checkServerChannels() {
        CoreLog.debug("DebugSuite#checkGlobalChannels(): Start.");
        Set<Identifier> channels = ServerPlayNetworking.getGlobalReceivers();
        Iterator<Identifier> iterator = channels.iterator();
        int i = 0;

        while (iterator.hasNext())
        {
            Identifier id = iterator.next();
            i++;
            CoreLog.debug("DebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        }
        CoreLog.debug("DebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    }
}
