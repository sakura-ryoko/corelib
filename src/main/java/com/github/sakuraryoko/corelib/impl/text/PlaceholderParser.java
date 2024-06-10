/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.sakuraryoko.corelib.impl.text;

//#if MC >= 11902
//$$ import eu.pb4.placeholders.api.PlaceholderContext;
//$$ import eu.pb4.placeholders.api.Placeholders;
//#else
import eu.pb4.placeholders.PlaceholderAPI;
//#endif
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@ApiStatus.Internal
public class PlaceholderParser
{
    public static Text parseTextCmdSrc(Text input, ServerCommandSource src)
    {
//#if MC >= 11902
        //$$ return Placeholders.parseText(input, PlaceholderContext.of(src));
//#else
        try
        {
            return PlaceholderAPI.parseText(input, src.getPlayer());
        }
        catch (Exception ignored)
        {
            return null;
        }
//#endif
    }

    public static Text parseTextPlayer(Text input, ServerPlayerEntity src)
    {
//#if MC >= 11902
        //$$ return Placeholders.parseText(input, PlaceholderContext.of(src));
//#else
        return PlaceholderAPI.parseText(input, src);
//#endif
    }

    public static Text parseTextServer(Text input, MinecraftServer src)
    {
//#if MC >= 11902
        //$$ return Placeholders.parseText(input, PlaceholderContext.of(src));
//#else
        return PlaceholderAPI.parseText(input, src);
//#endif
    }

    public static Text parseTextEntity(Text input, Entity src)
    {
//#if MC >= 11902
        //$$ return Placeholders.parseText(input, PlaceholderContext.of(src));
//#else
        if (src instanceof ServerPlayerEntity player)
        {
            return PlaceholderAPI.parseText(input, player);
        }

        return null;
//#endif
    }
}
