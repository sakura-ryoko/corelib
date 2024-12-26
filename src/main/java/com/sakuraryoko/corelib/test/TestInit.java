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

package com.sakuraryoko.corelib.test;

import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.modinit.IModInitDispatcher;
import com.sakuraryoko.corelib.api.modinit.ModInitData;
import com.sakuraryoko.corelib.api.text.ITextHandler;
import com.sakuraryoko.corelib.impl.text.BuiltinTextHandler;

public class TestInit implements IModInitDispatcher
{
    private static final TestInit INSTANCE = new TestInit();
    public static TestInit getInstance() { return INSTANCE; }
    private final ModInitData MOD_DATA = new ModInitData(TestReference.MOD_ID);
    public AnsiLogger LOGGER = new AnsiLogger(this.getClass(), this.isDebug());

    @Override
    public ModInitData getModInit()
    {
        return MOD_DATA;
    }

    @Override
    public String getModId()
    {
        return TestReference.MOD_ID;
    }

    @Override
    public ITextHandler getTextHandler()
    {
        return new BuiltinTextHandler();
    }

    @Override
    public boolean isDebug()
    {
        return TestReference.DEBUG;
    }

    @Override
    public void onModInit()
    {
        this.LOGGER.debug("ANSI Logger Test (ModID: {})", this.getModId());
    }
}
