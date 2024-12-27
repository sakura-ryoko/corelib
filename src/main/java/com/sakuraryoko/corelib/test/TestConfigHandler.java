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

import com.sakuraryoko.corelib.api.config.IConfigData;
import com.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.time.TimeFormat;
import com.sakuraryoko.corelib.impl.modinit.CoreInit;

public class TestConfigHandler implements IConfigDispatch
{
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass(), TestReference.DEBUG);
    private static final TestConfigHandler INSTANCE = new TestConfigHandler();
    public static TestConfigHandler getInstance() { return INSTANCE; }
    private final TestConfigData CONFIG = newConfig();
    private final String CONFIG_ROOT = TestReference.MOD_ID;
    private final String CONFIG_NAME = TestReference.MOD_ID;
    private boolean loaded = false;

    @Override
    public String getConfigRoot()
    {
        return this.CONFIG_ROOT;
    }

    @Override
    public boolean useRootDir()
    {
        return false;
    }

    @Override
    public String getConfigName()
    {
        return this.CONFIG_NAME;
    }

    @Override
    public TestConfigData newConfig()
    {
        return new TestConfigData();
    }

    @Override
    public IConfigData getConfig()
    {
        return this.CONFIG;
    }

    public TestConfigOptions getTestOptions()
    {
        return this.CONFIG.TEST;
    }

    @Override
    public boolean isLoaded()
    {
        return this.loaded;
    }

    @Override
    public void initConfig()
    {
        this.LOGGER.debug("initConfig()");
    }

    @Override
    public void onPreLoadConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostLoadConfig()
    {
        this.loaded = true;
    }

    @Override
    public void onPreSaveConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostSaveConfig()
    {
        this.loaded = true;
    }

    @Override
    public TestConfigData defaults()
    {
        TestConfigData config = this.newConfig();

        this.LOGGER.debug("defaults()");
        config.config_date = TimeFormat.RFC1123.formatNow(null);
        config.TEST.defaults();

        return config;
    }

    @Override
    public TestConfigData update(IConfigData newConfig)
    {
        TestConfigData newConf = (TestConfigData) newConfig;

        // Refresh
        this.LOGGER.debug("update()");
        CONFIG.comment = CoreInit.getInstance().getModVersionString() + " Config";
        CONFIG.config_date = TimeFormat.RFC1123.formatNow(null);
        this.LOGGER.info("update(): save_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        // Copy Main Config
        CONFIG.TEST.copy(newConf.TEST);

        return CONFIG;
    }

    @Override
    public void execute(boolean fromInit)
    {
        this.LOGGER.debug("execute()");
    }
}
