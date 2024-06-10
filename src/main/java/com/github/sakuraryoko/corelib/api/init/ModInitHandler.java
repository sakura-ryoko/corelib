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
