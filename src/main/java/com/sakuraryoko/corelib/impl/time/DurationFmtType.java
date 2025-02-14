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

import com.sakuraryoko.corelib.api.time.DurationFormat;
import com.sakuraryoko.corelib.impl.time.formatter.*;

@ApiStatus.Internal
public class DurationFmtType<T extends DurationFmt>
{
    public static final DurationFmtType<DurationFmtRegular> REGULAR;
    public static final DurationFmtType<DurationFmtPretty> PRETTY;
    public static final DurationFmtType<DurationFmtISOExtended> ISO_EXTENDED;
    public static final DurationFmtType<DurationFmtFormatted> FORMATTED;

    private final DurationFactory<? extends T> factory;
    private final DurationFormat durationFmt;

    private static <T extends DurationFmt> DurationFmtType<T> create(DurationFactory<? extends T> factory, DurationFormat durationFmt)
    {
        return new DurationFmtType<>(factory, durationFmt);
    }

    private DurationFmtType(DurationFactory<? extends T> factory, DurationFormat durationFmt)
    {
        this.durationFmt = durationFmt;
        this.factory = factory;
    }

    @Nullable
    public T init(DurationFormat fmt)
    {
        return this.factory.create(fmt);
    }

    public DurationFormat getFmt()
    {
        return this.durationFmt;
    }

    static
    {
        REGULAR = create(DurationFmtRegular::new, DurationFormat.REGULAR);
        PRETTY = create(DurationFmtPretty::new, DurationFormat.PRETTY);
        ISO_EXTENDED = create(DurationFmtISOExtended::new, DurationFormat.ISO_EXTENDED);
        FORMATTED = create(DurationFmtFormatted::new, DurationFormat.FORMATTED);
    }

    @FunctionalInterface
    interface DurationFactory<T extends DurationFmt>
    {
        T create(DurationFormat fmt);
    }
}
