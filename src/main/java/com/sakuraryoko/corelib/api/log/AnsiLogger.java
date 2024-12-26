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

package com.sakuraryoko.corelib.api.log;

public class AnsiLogger implements IAnsiLogger
{
    private final String log;
    private final boolean debug;

    public AnsiLogger(Class<?> clazz)
    {
        this(clazz, false);
    }

    public AnsiLogger(Class<?> clazz, boolean debug)
    {
        this.log = clazz.getName();
        this.debug = debug;
    }

    @Override
    public void info(String fmt, Object... args)
    {
        if (this.log != null)
        {
            String msg = this.format(fmt, args);
            System.out.printf(AnsiColors.WHITE + "[INFO/"+this.log+"]: %s" + AnsiColors.RESET+"\n", msg);
        }
    }

    @Override
    public void debug(String fmt, Object... args)
    {
        if (this.log != null && this.debug)
        {
            String msg = this.format(fmt, args);
            System.out.printf(AnsiColors.PURPLE_BOLD + "[DEBUG/"+this.log+"]: %s" + AnsiColors.RESET+"\n", msg);
        }
    }

    @Override
    public void warn(String fmt, Object... args)
    {
        if (this.log != null)
        {
            String msg = this.format(fmt, args);
            System.out.printf(AnsiColors.YELLOW_BOLD + "[WARN/"+this.log+"]: %s" + AnsiColors.RESET+"\n", msg);
        }
    }

    @Override
    public void error(String fmt, Object... args)
    {
        if (this.log != null)
        {
            String msg = this.format(fmt, args);
            System.out.printf(AnsiColors.RED_BOLD + "[ERROR/"+this.log+"]: %s" + AnsiColors.RESET+"\n", msg);
        }
    }

    @Override
    public void fatal(String fmt, Object... args)
    {
        if (this.log != null)
        {
            String msg = this.format(fmt, args);
            System.out.printf(AnsiColors.RED_BOLD_BRIGHT + "[FATAL/"+this.log+"]: %s" + AnsiColors.RESET+"\n", msg);
        }
    }
}
