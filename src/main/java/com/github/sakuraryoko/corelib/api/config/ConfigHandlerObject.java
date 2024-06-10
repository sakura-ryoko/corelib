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
