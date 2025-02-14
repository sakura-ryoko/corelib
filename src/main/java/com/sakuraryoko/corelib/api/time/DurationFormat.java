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

import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;

import com.sakuraryoko.corelib.impl.time.DurationFmtType;
import com.sakuraryoko.corelib.impl.time.formatter.DurationFmt;

public enum DurationFormat
{
    REGULAR      ("regular",      DurationFmtType.REGULAR),
    PRETTY       ("pretty",       DurationFmtType.PRETTY),
    ISO_EXTENDED ("iso_extended", DurationFmtType.ISO_EXTENDED),
    FORMATTED    ("formatted",    DurationFmtType.FORMATTED);

    public static final ImmutableList<DurationFormat> VALUES = ImmutableList.copyOf(values());

    final String name;
    final DurationFmtType<?> type;

    DurationFormat(String name, DurationFmtType<?> type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName() { return this.name; }

    public DurationFmtType<?> getType()
    {
        return this.type;
    }

    public @Nullable DurationFmt init()
    {
        return this.type.init(this);
    }

    public String format(long duration)
    {
        return this.format(duration, null);
    }

    public String format(long duration, @Nullable String fmt)
    {
        DurationFmt formatter = this.init();

        if (formatter != null)
        {
            if (duration < 1)
            {
                return "Invalid Duration ["+duration+"]";
            }

            return formatter.format(duration, fmt);
        }

        return "";
    }

    public String getFormatString()
    {
        DurationFmt formatter = this.init();

        if (formatter != null)
        {
            return formatter.getFormatString();
        }

        return "";
    }

    @Nullable
    public DurationFormat fromString(String value)
    {
        return fromStringStatic(value);
    }

    @Nullable
    public static DurationFormat fromStringStatic(String name)
    {
        for (DurationFormat val : VALUES)
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
