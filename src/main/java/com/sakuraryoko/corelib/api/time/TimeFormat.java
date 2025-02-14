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

package com.sakuraryoko.corelib.api.time;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;

import com.sakuraryoko.corelib.impl.time.TimeFmtType;
import com.sakuraryoko.corelib.impl.time.formatter.TimeFmt;

public enum TimeFormat
{
    REGULAR     ("regular", TimeFmtType.REGULAR),
    ISO_LOCAL   ("iso_local",  TimeFmtType.ISO_LOCAL),
    ISO_OFFSET  ("iso_offset", TimeFmtType.ISO_OFFSET),
    FORMATTED   ("formatted",  TimeFmtType.FORMATTED),
    RFC1123     ("rfc1123",    TimeFmtType.RFC1123);

    public static final ImmutableList<TimeFormat> VALUES = ImmutableList.copyOf(values());

    final String name;
    final TimeFmtType<?> type;

    TimeFormat(String name, TimeFmtType<?> type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName() { return this.name; }

    public TimeFmtType<?> getType()
    {
        return this.type;
    }

    public @Nullable TimeFmt init()
    {
        return this.type.init(this);
    }

    public String formatTo(long time)
    {
        return this.formatTo(time, null);
    }

    public String formatTo(long time, @Nullable String fmt)
    {
        TimeFmt formatter = this.init();

        if (formatter != null)
        {
            return formatter.formatTo(time, fmt);
        }

        return "";
    }

    public long formatFrom(@Nonnull String formattedTime)
    {
        return this.formatFrom(formattedTime, null);
    }

    public long formatFrom(@Nonnull String formattedTime, @Nullable String fmt)
    {
        TimeFmt formatter = this.init();

        if (formatter != null)
        {
            return formatter.formatFrom(formattedTime, fmt);
        }

        return 0L;
    }

    public String formatNow()
    {
        return this.formatNow(null);
    }

    public String formatNow(@Nullable String fmt)
    {
        TimeFmt formatter = this.init();

        if (formatter != null)
        {
            return formatter.formatNow(fmt);
        }

        return "";
    }

    public String getFormatString()
    {
        TimeFmt formatter = this.init();

        if (formatter != null)
        {
            return formatter.getFormatString();
        }

        return "";
    }

    @Nullable
    public TimeFormat fromString(String value)
    {
        return fromStringStatic(value);
    }

    @Nullable
    public static TimeFormat fromStringStatic(String name)
    {
        for (TimeFormat val : VALUES)
        {
            if (name.compareToIgnoreCase(val.getName()) == 0)
            {
                return val;
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }
}
