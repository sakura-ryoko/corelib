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

package com.sakuraryoko.corelib.impl.modinit;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.modinit.IModInitDispatcher;
import com.sakuraryoko.corelib.impl.Reference;

public class ModInitManager implements IModInitManager
{
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass(), Reference.DEBUG);
    private static final ModInitManager INSTANCE = new ModInitManager();
    public static IModInitManager getInstance() { return INSTANCE; }
    private final List<IModInitDispatcher> DISPATCH = new ArrayList<>();

    @Override
    public void registerModInitHandler(IModInitDispatcher handler) throws RuntimeException
    {
        this.LOGGER.debug("registerModInitHandler()");

        if (!this.DISPATCH.contains(handler))
        {
            this.DISPATCH.add(handler);
            if (!handler.isInitComplete()) { handler.onModInit(); }
        }
        else
        {
            throw new RuntimeException("Mod Init Dispatcher already registered!");
        }
    }

    @ApiStatus.Internal
    public void onModInit()
    {
        this.LOGGER.debug("onModInit()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) ->
            {
                if (!handler.isInitComplete()) { handler.onModInit(); }
            });
        }
    }

    @ApiStatus.Internal
    public void setIntegratedServer(boolean toggle)
    {
        this.LOGGER.debug("setIntegratedServer()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.setIntegratedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setDedicatedServer(boolean toggle)
    {
        this.LOGGER.debug("setDedicatedServer()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.setDedicatedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setOpenToLan(boolean toggle)
    {
        this.LOGGER.debug("setOpenToLan()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach((handler) -> handler.setOpenToLan(toggle));
        }
    }

    @ApiStatus.Internal
    public void reset()
    {
        this.LOGGER.debug("reset()");

        if (!this.DISPATCH.isEmpty())
        {
            this.DISPATCH.forEach(IModInitDispatcher::reset);
        }
    }
}
