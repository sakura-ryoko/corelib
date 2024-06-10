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

package com.github.sakuraryoko.corelib.api.init;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

public class ModInitHandler implements IModInitManager
{
    private static final ModInitHandler INSTANCE = new ModInitHandler();
    private final List<IModInitDispatch> handlers = new ArrayList<>();
    public static IModInitManager getInstance() { return INSTANCE; }

    @Override
    public void registerModInitHandler(IModInitDispatch handler)
    {
        if (!this.handlers.contains(handler))
        {
            this.handlers.add(handler);
        }
    }

    @ApiStatus.Internal
    public void onModInit()
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IModInitDispatch::onModInit);
        }
    }

    @ApiStatus.Internal
    public void setIntegratedServer(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setIntegratedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setDedicatedServer(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setDedicatedServer(toggle));
        }
    }

    @ApiStatus.Internal
    public void setOpenToLan(boolean toggle)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((handler) -> handler.setOpenToLan(toggle));
        }
    }

    @ApiStatus.Internal
    public void reset()
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach(IModInitDispatch::reset);
        }
    }
}
