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

public interface IAnsiLogger
{
    default String format(final String format, final Object... args)
    {
        String result = format;

        for (Object arg : args)
        {
            result = result.replaceFirst("\\{\\}", arg.toString());
        }

        return result;
    }

    void info(String fmt, Object... args);

    void debug(String fmt, Object... args);

    void warn(String fmt, Object... args);

    void error(String fmt, Object... args);

    void fatal(String fmt, Object... args);
}
