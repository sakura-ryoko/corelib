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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.sakuraryoko.corelib.api.time.TimeFormat;
import com.sakuraryoko.corelib.impl.CoreLib;

@ApiStatus.Internal
public class TimeFmtFormatted extends TimeFmt
{
    private final DateTimeFormatter formatter;

    public TimeFmtFormatted(TimeFormat fmt)
    {
        super(fmt);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);
        this.formatString = "yyyy-MM-dd_HH.mm.ss";
    }

    @Override
    public String formatTo(long time, @Nullable String fmt)
    {
        if (fmt != null && !fmt.isEmpty())
        {
            DateTimeFormatter format = this.setFormat(fmt);

            if (format != null)
            {
                return format.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
            }
        }

        return this.formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    @Override
    public long formatFrom(@Nonnull String formatted, @Nullable String fmt)
    {
        DateTimeFormatter format;
        LocalDateTime dateTime;

        if (fmt != null)
        {
            format = this.setFormat(fmt);

            if (format == null)
            {
                return 0L;
            }
        }
        else
        {
            format = this.formatter;
        }

        try
        {
            dateTime = LocalDateTime.parse(formatted, format);
        }
        catch (Exception err)
        {
            CoreLib.LOGGER.error("fromFormat(): Invalid Date/Time format // {}", err.getMessage());
            return 0L;
        }

        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public String formatNow(@Nullable String fmt)
    {
        if (fmt != null && !fmt.isEmpty())
        {
            DateTimeFormatter format = this.setFormat(fmt);

            if (format != null)
            {
                return format.format(ZonedDateTime.now());
            }
        }

        return this.formatter.format(ZonedDateTime.now());
    }

    private @Nullable DateTimeFormatter setFormat(@Nonnull String fmt)
    {
        DateTimeFormatter temp;

        try
        {
            temp = DateTimeFormatter.ofPattern(fmt, Locale.ROOT);
        }
        catch (IllegalArgumentException err)
        {
            CoreLib.LOGGER.error("setFormat(): Invalid Date/Time format // {}", err.getMessage());
            return null;
        }

        this.formatString = fmt;
        return temp;
    }
}
