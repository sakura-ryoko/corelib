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
