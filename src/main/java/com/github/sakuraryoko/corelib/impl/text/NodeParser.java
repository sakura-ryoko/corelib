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

package com.github.sakuraryoko.corelib.impl.text;

//#if MC >= 11902
//$$ import eu.pb4.placeholders.api.TextParserUtils;
//#else
import eu.pb4.placeholders.TextParser;
//#endif
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.text.Text;

@ApiStatus.Internal
public class NodeParser
{
    public static Text parse(String input)
    {
        return parse(input, true);
    }

    public static Text parse(String input, boolean safe)
    {
//#if MC >= 11902
        //$$ if (safe)
        //$$ {
            //$$ return TextParserUtils.formatTextSafe(input);
        //$$ }

        //$$ return TextParserUtils.formatText(input);
//#else
        if (safe)
        {
            return TextParser.parseSafe(input);
        }

        return TextParser.parse(input);
//#endif
    }
}
