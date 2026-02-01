/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.core.config;

import com.sakuraryoko.corelib.api.config.IConfigData;
import com.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.time.TimeFormat;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.core.config.data.CoreConfigData;
import com.sakuraryoko.corelib.impl.core.modinit.CoreInit;

public class CoreConfigHandler implements IConfigDispatch
{
	private final AnsiLogger LOGGER = new AnsiLogger(this.getClass(), Reference.DEBUG);
	private static final CoreConfigHandler INSTANCE = new CoreConfigHandler();
	public static CoreConfigHandler getInstance() { return INSTANCE; }
	private final CoreConfigData CONFIG = newConfig();
	private final String CONFIG_ROOT = Reference.MOD_ID;
	private final String CONFIG_NAME = Reference.MOD_ID;
	private boolean loaded = false;

	@Override
	public String getConfigRoot()
	{
		return this.CONFIG_ROOT;
	}

	@Override
	public boolean useRootDir()
	{
		return true;
	}

	@Override
	public String getConfigName()
	{
		return this.CONFIG_NAME;
	}

	@Override
	public CoreConfigData newConfig()
	{
		return new CoreConfigData();
	}

	@Override
	public CoreConfigData getConfig()
	{
		return this.CONFIG;
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
	public CoreConfigData defaults()
	{
		CoreConfigData config = this.newConfig();

		this.LOGGER.debug("defaults()");
		config.config_date = TimeFormat.RFC1123.formatNow();
		config.core.defaults();

		return config;
	}

	@Override
	public CoreConfigData update(IConfigData newConfig)
	{
		CoreConfigData newConf = (CoreConfigData) newConfig;

		// Refresh
		this.LOGGER.debug("update()");
		CONFIG.comment = CoreInit.getInstance().getModVersionString() + " Config";
		CONFIG.config_date = TimeFormat.RFC1123.formatNow();
		this.LOGGER.info("update(): save_date: {} --> {}", newConf.config_date, CONFIG.config_date);

		// Copy Main Config
		CONFIG.core.copy(newConf.core);

		return CONFIG;
	}

	@Override
	public void execute(boolean fromInit)
	{
		this.LOGGER.debug("execute()");
	}
}
