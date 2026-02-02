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

package com.sakuraryoko.corelib.impl.core.modinit;

import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.modinit.IModInitDispatcher;
import com.sakuraryoko.corelib.api.modinit.ModInitData;
import com.sakuraryoko.corelib.api.text.ITextHandler;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.config.ConfigManager;
import com.sakuraryoko.corelib.impl.core.config.CoreConfigHandler;
import com.sakuraryoko.corelib.impl.events.tick.TickManager;
import com.sakuraryoko.corelib.impl.network.announcer.CoreServiceHandler;
import com.sakuraryoko.corelib.impl.network.thread.CoreNetworkThreadHandler;
import com.sakuraryoko.corelib.impl.text.BuiltinTextHandler;

@ApiStatus.Internal
public class CoreInit implements IModInitDispatcher
{
    private static final CoreInit INSTANCE = new CoreInit();
    public static CoreInit getInstance() { return INSTANCE; }
    private static final ModInitData MOD_DATA = new ModInitData(Reference.MOD_ID);
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass(), this.isDebug());
    private boolean INIT = false;

    @Override
    public ModInitData getModInit()
    {
        return MOD_DATA;
    }

    @Override
    public String getModId()
    {
        return Reference.MOD_ID;
    }

    @Override
    public ITextHandler getTextHandler()
    {
        return BuiltinTextHandler.getInstance();
    }

    @Override
    public boolean isDebug()
    {
        return Reference.DEBUG;
    }

    @Override
    public boolean isInitComplete()
    {
        return this.INIT;
    }

    @Override
    public void onModInit()
    {
        this.LOGGER.debug("CoreLib Init (ModID: {})", this.getModId());

        if (Reference.EXPERIMENTAL)
        {
            ConfigManager.getInstance().registerConfigDispatcher(CoreConfigHandler.getInstance());
            TickManager.getInstance().registerTickHandler(CoreNetworkThreadHandler.getInstance());
            CoreNetworkThreadHandler.getInstance().start();

            CoreServiceHandler.getInstance().onRegisterPayloads();
            CoreServiceHandler.getInstance().registerPacketListeners();
        }

        this.INIT = true;
    }
}
