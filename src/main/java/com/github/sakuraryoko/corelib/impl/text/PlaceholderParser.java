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
