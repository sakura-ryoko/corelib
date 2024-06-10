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

package com.github.sakuraryoko.corelib.util;

import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoreLog
{
    private static final String CORE_ID = CoreInitHandler.getInstance().getModId();
    private static final boolean CORE_DEBUG = CoreInitHandler.getInstance().isDebug();
    private static final boolean CORE_WRAP_ID = CoreInitHandler.getInstance().isWrapID();
    private static final Logger LOGGER = LogManager.getLogger(CORE_ID);

    public static void debug(String msg, Object... args)
    {
        if (CORE_DEBUG)
        {
            final String wrapDebug = CORE_WRAP_ID ? "["+CORE_ID+":DEBUG] "+msg : "[DEBUG] "+msg;
            LOGGER.info(wrapDebug, args);
        }
        else
        {
            LOGGER.debug(wrap(msg), args);
        }
    }

    public static void info(String msg, Object... args)
    {
        LOGGER.info(wrap(msg), args);
    }

    public static void warn(String msg, Object... args)
    {
        LOGGER.warn(wrap(msg), args);
    }

    public static void error(String msg, Object... args)
    {
        LOGGER.error(wrap(msg), args);
    }

    public static void fatal(String msg, Object... args)
    {
        LOGGER.fatal(wrap(msg), args);
    }

    private static String wrap(String msg)
    {
        if (CORE_WRAP_ID)
        {
            return "["+CORE_ID+"] "+msg;
        }

        return msg;
    }
}
