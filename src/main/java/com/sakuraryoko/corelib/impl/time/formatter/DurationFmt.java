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

package com.sakuraryoko.corelib.impl.time.formatter;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.sakuraryoko.corelib.api.time.DurationFormat;

@ApiStatus.Internal
public abstract class DurationFmt
{
    protected DurationFormat type;
    protected String formatString;

    public DurationFmt(DurationFormat fmt)
    {
        this.formatString = "";
        this.type = fmt;
    }

    public DurationFormat getType()
    {
        return this.type;
    }

    public String format(long duration) { return this.format(duration, null); }

    public String format(long duration, @Nullable String fmt) { return ""; }

    public String getFormatString()
    {
        return this.formatString;
    }
}
