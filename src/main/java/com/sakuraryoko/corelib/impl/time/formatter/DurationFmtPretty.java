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

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.sakuraryoko.corelib.api.time.DurationFormat;
import com.sakuraryoko.corelib.impl.CoreLib;

@ApiStatus.Internal
public class DurationFmtPretty extends DurationFmt
{
    public DurationFmtPretty(DurationFormat fmt)
    {
        super(fmt);
        this.formatString = "d' days 'H' hours 'm' minutes 's' seconds'";
    }

    @Override
    public String format(long duration, @Nullable String fmt)
    {
        try
        {
            return DurationFormatUtils.formatDurationWords(duration, true, true);
        }
        catch (Exception err)
        {
            CoreLib.LOGGER.error("format(): Invalid Duration format // {}", err.getMessage());
        }

        return DurationFormatUtils.formatDurationHMS(duration);
    }
}
