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

import java.util.Random;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;

import com.sakuraryoko.corelib.api.time.DurationFormat;
import com.sakuraryoko.corelib.api.time.TimeFormat;
import com.sakuraryoko.corelib.impl.text.BuiltinTextHandler;

@ApiStatus.Internal
public class TimeTestExample
{
    public static Component runDurationTest()
    {
        return runDurationTest(false);
    }

    public static Component runDurationTest(boolean placeholder)
    {
        StringBuilder result = new StringBuilder("*** Duration Test:\n");
        long duration = new Random(Util.getMillis()).nextInt();

        // Cannot use a negative duration.
        while (duration < 1)
        {
            duration = new Random(Util.getMillis()).nextInt();
        }

        result.append("Random Value: ").append(duration).append("\n");

        for (DurationFormat format : DurationFormat.VALUES)
        {
            if (placeholder)
            {
                result.append(format.getName().toUpperCase()).append(": <green>(").append(format.format(duration)).append(")</green> // [Format: <yellow>").append(format.getFormatString()).append("]<r>\n");
            }
            else
            {
                result.append(format.getName().toUpperCase()).append(": §a(").append(format.format(duration)).append(")§r // [Format: §e").append(format.getFormatString()).append("]§r\n");
            }
        }

        return BuiltinTextHandler.getInstance().formatTextSafe(result.toString());
    }

    public static Component runTimeDateTest()
    {
        return runTimeDateTest(false);
    }

    public static Component runTimeDateTest(boolean placeholder)
    {
        StringBuilder result = new StringBuilder("*** Time/Date Test:\n");

        result.append("Vanilla Formatted Now: ").append(Util.getPlatform()).append("\n");

        for (TimeFormat format : TimeFormat.VALUES)
        {
            if (placeholder)
            {
                result.append(format.getName().toUpperCase()).append(": <green>(").append(format.formatNow()).append(")</green> // [Format: <yellow>").append(format.getFormatString()).append("]<r>\n");
            }
            else
            {
                result.append(format.getName().toUpperCase()).append(": §a(").append(format.formatNow()).append(")§r // [Format: §e").append(format.getFormatString()).append("]§r\n");
            }
        }

        return BuiltinTextHandler.getInstance().formatTextSafe(result.toString());
    }
}
