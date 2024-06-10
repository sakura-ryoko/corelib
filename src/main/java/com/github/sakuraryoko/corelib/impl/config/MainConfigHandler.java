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

package com.github.sakuraryoko.corelib.impl.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.github.sakuraryoko.corelib.api.config.IConfigData;
import com.github.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MainConfigHandler implements IConfigDispatch
{
    private static final MainConfigHandler INSTANCE = new MainConfigHandler();
    public static MainConfigHandler getInstance() { return INSTANCE; }
    public static MainConfigData CONFIG = new MainConfigData();
    private boolean loaded = false;

    @Override
    public MainConfigData newConfig()
    {
        return new MainConfigData();
    }

    @Override
    public MainConfigData getConfig()
    {
        return CONFIG;
    }

    @Override
    public boolean isLoaded()
    {
        return this.loaded;
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
    public MainConfigData defaults()
    {
        MainConfigData conf = this.newConfig();
        CoreLog.debug("MainConfigHandler.defaults() has been called.");

        conf.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());

        return conf;
    }

    @Override
    public MainConfigData update(IConfigData newConfig)
    {
        MainConfigData newConf = (MainConfigData) newConfig;
        CoreLog.debug("MainConfigHandler.refresh() has been called.");

        // Refresh Data from Config
        CONFIG.config_date = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT).format(ZonedDateTime.now());
        CoreLog.debug("MainConfigHandler.refresh(): config_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        return CONFIG;
    }

    @Override
    public void execute()
    {
        CoreLog.debug("MainConfigHandler.execute() has been called.");

        // Do this when the Main Config gets reloaded.
        CoreLog.debug("MainConfigHandler.execute(): new config_date: {}", CONFIG.config_date);
    }
}
