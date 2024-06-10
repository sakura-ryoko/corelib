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

package com.github.sakuraryoko.corelib.impl.network;

import java.util.Iterator;
import java.util.Set;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.util.CoreLog;
//#if MC >= 12006
//$$ import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//$$ import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TestSuite
{
    //#if MC >= 12006
    //$$ public static void testS2C(ServerPlayerEntity player, String msg)
    //$$ {
        //$$ if (CoreInitHandler.getInstance().isServer())
        //$$ {
        //$$ }
        //$$ else
        //$$ {
            //$$ CoreLog.info("testS2C() called from a Client Environment.");
        //$$ }
    //$$ }

    //$$ public static void testC2S(String msg)
    //$$ {
        //$$ if (CoreInitHandler.getInstance().isClient())
        //$$ {
        //$$ }
        //$$ else
        //$$ {
            //$$ CoreLog.info("testC2S() called from a Server Environment.");
        //$$ }
    //$$ }

    //$$ public static void checkClientChannels()
    //$$ {
        //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): Start.");
        //$$ Set<Identifier> channels = ClientPlayNetworking.getGlobalReceivers();
        //$$ Iterator<Identifier> iterator = channels.iterator();
        //$$ int i = 0;

        //$$ while (iterator.hasNext())
        //$$ {
            //$$ Identifier id = iterator.next();
            //$$ i++;
            //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        //$$ }

        //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    //$$ }

    //$$ public static void checkServerChannels()
    //$$ {
        //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): Start.");
        //$$ Set<Identifier> channels = ServerPlayNetworking.getGlobalReceivers();
        //$$ Iterator<Identifier> iterator = channels.iterator();
        //$$ int i = 0;

        //$$ while (iterator.hasNext())
        //$$ {
            //$$ Identifier id = iterator.next();
            //$$ i++;
            //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): id("+i+") hash: "+id.hashCode()+" //name: "+id.getNamespace()+" path: "+id.getPath());
        //$$ }

        //$$ CoreLog.debug("DebugSuite#checkGlobalChannels(): END. Total Channels: "+i);
    //$$ }
    //#endif
}
