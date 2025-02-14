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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.time.TimeFormat;

@ApiStatus.Internal
public abstract class TimeFmt
{
    protected String formatString;
    protected TimeFormat type;

    public TimeFmt(TimeFormat fmt)
    {
        this.type = fmt;
        this.formatString = "";
    }

    public TimeFormat getType()
    {
        return this.type;
    }

    public String formatTo(long time) { return this.formatTo(time, null); }

    public String formatTo(long time, @Nullable String fmt) { return ""; }

    public long formatFrom(@Nonnull String formattedTime) { return this.formatFrom(formattedTime, null); }

    public long formatFrom(@Nonnull String formattedTime, @Nullable String fmt) { return 0L; }

    public String formatNow() { return this.formatNow(null); }

    public String formatNow(@Nullable String fmt) { return ""; }

    public String getFormatString()
    {
        return this.formatString;
    }
}
