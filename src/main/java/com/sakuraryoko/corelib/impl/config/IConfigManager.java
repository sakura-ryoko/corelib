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

import com.sakuraryoko.corelib.api.config.IConfigDispatch;

public interface IConfigManager
{
    void registerConfigDispatcher(IConfigDispatch handler) throws RuntimeException;

    // Generic
    void initAllConfigs();
    void defaultAllConfigs();
    void loadAllConfigs();
    void saveAllConfigs();
    void reloadAllConfigs();

    // Individual
    void initEach(IConfigDispatch handler) throws RuntimeException;
    void defaultEach(IConfigDispatch handler) throws RuntimeException;
    void loadEach(IConfigDispatch handler) throws RuntimeException;
    void saveEach(IConfigDispatch handler) throws RuntimeException;
    void reloadEach(IConfigDispatch handler) throws RuntimeException;
}
