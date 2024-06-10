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

package com.github.sakuraryoko.corelib.api.config;

public class ConfigHandlerObject
{
    private final IConfigDispatch handler;
    private final IConfigData type;
    private final String modid;
    private final String configFile;
    private final String key;
    private final boolean rootDir;

    public ConfigHandlerObject(IConfigDispatch handler, IConfigData data, String modid)
    {
        this.handler = handler;
        this.modid = modid;
        this.configFile = modid;
        this.key = modid + ":" + modid;
        this.rootDir = false;
        this.type = data;
    }

    public ConfigHandlerObject(IConfigDispatch handler, IConfigData data, String modid, boolean rootDir)
    {
        this.handler = handler;
        this.modid = modid;
        this.configFile = modid;
        this.key = modid + ":" + modid;
        this.rootDir = rootDir;
        this.type = data;
    }

    public ConfigHandlerObject(IConfigDispatch handler, IConfigData data, String modid, String file)
    {
        this.handler = handler;
        this.modid = modid;
        this.configFile = file;
        this.key = modid + ":" + file;
        this.rootDir = false;
        this.type = data;
    }

    public ConfigHandlerObject(IConfigDispatch handler, IConfigData data, String modid, String file, boolean rootDir)
    {
        this.handler = handler;
        this.modid = modid;
        this.configFile = file;
        this.key = modid + ":" + file;
        this.rootDir = rootDir;
        this.type = data;
    }

    public IConfigDispatch getHandler() { return this.handler; }

    public IConfigData getDataType() { return this.type; }

    public String getModid() { return this.modid; }

    public String getConfigFile() { return this.configFile; }

    public String getKey() { return this.key; }

    public boolean getRoorDir() { return this.rootDir; }
}
