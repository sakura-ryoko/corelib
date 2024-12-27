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

package com.sakuraryoko.corelib.impl.time;

import javax.annotation.Nullable;

import org.jetbrains.annotations.ApiStatus;

import com.sakuraryoko.corelib.api.time.TimeFormat;
import com.sakuraryoko.corelib.impl.time.formatter.*;

@ApiStatus.Internal
public class TimeFmtType<T extends TimeFmt>
{
    public static final TimeFmtType<TimeFmtRegular> REGULAR;
    public static final TimeFmtType<TimeFmtISOLocal> ISO_LOCAL;
    public static final TimeFmtType<TimeFmtISOOffset> ISO_OFFSET;
    public static final TimeFmtType<TimeFmtFormatted> FORMATTED;
    public static final TimeFmtType<TimeFmtRFC1123> RFC1123;

    private final TimeFactory<? extends T> factory;
    private final TimeFormat timeFmt;

    @SuppressWarnings("unchecked")
    private static <T extends TimeFmt> TimeFmtType<T> create(TimeFactory<? extends T> factory, TimeFormat timeFmt)
    {
        return (TimeFmtType) new TimeFmtType(factory, timeFmt);
    }

    private TimeFmtType(TimeFactory<? extends T> factory, TimeFormat timeFmt)
    {
        this.timeFmt = timeFmt;
        this.factory = factory;
    }

    @Nullable
    public T init(TimeFormat fmt)
    {
        return this.factory.create(fmt);
    }

    public TimeFormat getFmt()
    {
        return this.timeFmt;
    }

    static
    {
        REGULAR = create(TimeFmtRegular::new, TimeFormat.REGULAR);
        ISO_LOCAL = create(TimeFmtISOLocal::new, TimeFormat.ISO_LOCAL);
        ISO_OFFSET = create(TimeFmtISOOffset::new, TimeFormat.ISO_OFFSET);
        FORMATTED = create(TimeFmtFormatted::new, TimeFormat.FORMATTED);
        RFC1123 = create(TimeFmtRFC1123::new, TimeFormat.RFC1123);
    }

    @FunctionalInterface
    interface TimeFactory<T extends TimeFmt>
    {
        T create(TimeFormat fmt);
    }
}
