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
