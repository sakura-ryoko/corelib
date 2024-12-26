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

package com.sakuraryoko.corelib.impl.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.config.IConfigData;
import com.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.sakuraryoko.corelib.impl.CoreLib;
import com.sakuraryoko.corelib.impl.Reference;

public class ConfigManager implements IConfigManager
{
    private static final ConfigManager INSTANCE = new ConfigManager();
    public static IConfigManager getInstance() { return INSTANCE; }
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final List<IConfigDispatch> DISPATCH = new ArrayList<>();

    @Override
    public void registerConfigDispatcher(IConfigDispatch dispatch) throws RuntimeException
    {
        if (!this.DISPATCH.contains(dispatch))
        {
            this.DISPATCH.add(dispatch);
            this.initEach(dispatch);
            dispatch.onPreLoadConfig();
            this.loadEachInternal(dispatch, true);
            dispatch.onPostLoadConfig();
        }
        else
        {
            throw new RuntimeException("Config Dispatch has already been registered");
        }
    }

    @Override
    public void initAllConfigs()
    {
        for (IConfigDispatch dispatch : this.DISPATCH)
        {
            dispatch.initConfig();
        }
    }

    @Override
    public void loadAllConfigs()
    {
        for (IConfigDispatch dispatch : this.DISPATCH)
        {
            dispatch.onPreLoadConfig();
            this.loadEachInternal(dispatch, false);
            dispatch.onPostLoadConfig();
        }
    }

    @Override
    public void defaultAllConfigs()
    {
        for (IConfigDispatch dispatch : this.DISPATCH)
        {
            dispatch.onPreLoadConfig();
            dispatch.update(dispatch.defaults());
            dispatch.onPostLoadConfig();
            dispatch.execute(true);
        }
    }

    @Override
    public void saveAllConfigs()
    {
        for (IConfigDispatch dispatch : this.DISPATCH)
        {
            if (dispatch.isLoaded())
            {
                dispatch.onPreSaveConfig();
                this.saveEachInternal(dispatch);
                dispatch.onPostSaveConfig();
            }
        }
    }

    @Override
    public void reloadAllConfigs()
    {
        for (IConfigDispatch dispatch : this.DISPATCH)
        {
            dispatch.onPreLoadConfig();
            this.loadEachInternal(dispatch, false);
            dispatch.onPostLoadConfig();
        }
    }

    @Override
    public void initEach(IConfigDispatch handler) throws RuntimeException
    {
        if (this.DISPATCH.contains(handler))
        {
            handler.initConfig();
        }
        else
        {
            throw new RuntimeException("Config Dispatch has not been registered");
        }
    }

    @Override
    public void defaultEach(IConfigDispatch handler) throws RuntimeException
    {
        if (this.DISPATCH.contains(handler))
        {
            handler.onPreLoadConfig();
            handler.update(handler.defaults());
            handler.onPostLoadConfig();
            handler.execute(true);
        }
        else
        {
            throw new RuntimeException("Config Dispatch has not been registered");
        }
    }

    @Override
    public void loadEach(IConfigDispatch handler) throws RuntimeException
    {
        if (this.DISPATCH.contains(handler))
        {
            handler.onPreLoadConfig();
            this.loadEachInternal(handler, false);
            handler.onPostLoadConfig();
        }
        else
        {
            throw new RuntimeException("Config Dispatch has not been registered");
        }
    }

    @Override
    public void saveEach(IConfigDispatch handler) throws RuntimeException
    {
        if (this.DISPATCH.contains(handler))
        {
            if (handler.isLoaded())
            {
                handler.onPreSaveConfig();
                this.saveEachInternal(handler);
                handler.onPostSaveConfig();
            }
            else
            {
                handler.onPreLoadConfig();
                this.loadEachInternal(handler, false);
                handler.onPostLoadConfig();
            }
        }
        else
        {
            throw new RuntimeException("Config Dispatch has not been registered");
        }
    }

    @Override
    public void reloadEach(IConfigDispatch handler) throws RuntimeException
    {
        if (this.DISPATCH.contains(handler))
        {
            handler.onPreLoadConfig();
            this.loadEachInternal(handler, false);
            handler.onPostLoadConfig();
        }
        else
        {
            throw new RuntimeException("Config Dispatch has not been registered");
        }
    }

    @ApiStatus.Internal
    private void loadEachInternal(IConfigDispatch config, boolean fromInit)
    {
        IConfigData conf = config.newConfig();
        CoreLib.debugLog("loadEach(): --> [{}/{}.json]", config.getConfigRoot(), config.getConfigName());

        try
        {
            Path dir;

            if (config.useRootDir())
            {
                dir = Reference.CONFIG_DIR;
            }
            else
            {
                dir = Reference.CONFIG_DIR.resolve(config.getConfigRoot());
            }

            if (!Files.isDirectory(dir))
            {
                Files.createDirectory(dir);
            }

            Path file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                JsonElement data = JsonParser.parseString(Files.readString(file));
                conf = GSON.fromJson(data, conf.getClass());
                CoreLib.LOGGER.info("loadEach(): Read config for [{}/{}]", dir.getFileName().toString(), file.getFileName().toString());
            }
            else
            {
                conf = config.defaults();
                CoreLib.LOGGER.info("loadEach(): Config defaults for [{}/{}.json]", config.getConfigRoot(), config.getConfigName());
            }

            conf = config.update(conf);
            Files.writeString(file, GSON.toJson(conf));
            config.execute(false);
        }
        catch (Exception e)
        {
            CoreLib.LOGGER.error("loadEach(): Error reading config [{}/{}.json] // {}", config.getConfigRoot(), config.getConfigName(), e.getMessage());
        }
    }

    @ApiStatus.Internal
    private void saveEachInternal(IConfigDispatch config)
    {
        IConfigData conf;

        CoreLib.debugLog("saveEach(): --> [{}/{}.json]", config.getConfigRoot(), config.getConfigName());

        try
        {
            Path dir;

            if (config.useRootDir())
            {
                dir = Reference.CONFIG_DIR;
            }
            else
            {
                dir = Reference.CONFIG_DIR.resolve(config.getConfigRoot());
            }

            if (!Files.isDirectory(dir))
            {
                Files.createDirectory(dir);
            }

            Path file = dir.resolve(config.getConfigName() + ".json");

            if (Files.exists(file))
            {
                CoreLib.LOGGER.info("saveEach(): Deleting existing config file: [{}/{}]", dir.getFileName().toString(), file.getFileName().toString());
                Files.delete(file);
            }

            conf = config.getConfig();

            if (conf != null)
            {
                Files.writeString(file, GSON.toJson(conf));
                CoreLib.LOGGER.info("saveEach(): Wrote config for [{}/{}.json]", config.getConfigRoot(), config.getConfigName());
            }
            else
            {
                CoreLib.LOGGER.error("saveEach(): Error saving config file [{}/{}.json] // config is empty!", config.getConfigRoot(), config.getConfigName());
            }
        }
        catch (Exception e)
        {
            CoreLib.LOGGER.error("saveEach(): Error saving config file [{}/{}.json] // {}", config.getConfigRoot(), config.getConfigName(), e.getMessage());
        }
    }
}
