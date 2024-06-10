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

package com.github.sakuraryoko.corelib.api.text;

import com.github.sakuraryoko.corelib.impl.text.NodeParser;
import com.github.sakuraryoko.corelib.impl.text.PlaceholderParser;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TextParser
{
    private static final TextParser INSTANCE = new TextParser();
    public static TextParser getInstance() { return INSTANCE; }

    public Text parse(String input, ServerCommandSource src)
    {
        return this.parsePlaceholders(this.parseNodes(input, true), src);
    }

    public Text parse(String input, ServerPlayerEntity src)
    {
        return this.parsePlaceholders(this.parseNodes(input, true), src);
    }

    public Text parse(String input, MinecraftServer src)
    {
        return this.parsePlaceholders(this.parseNodes(input, true), src);
    }

    public Text parse(String input, Entity src)
    {
        return this.parsePlaceholders(this.parseNodes(input, true), src);
    }

    public Text parseNodes(String input)
    {
        return NodeParser.parse(input, true);
    }

    public Text parseNodes(String input, boolean safe)
    {
        return NodeParser.parse(input, safe);
    }

    public Text parsePlaceholders(Text input, ServerCommandSource src)
    {
        return PlaceholderParser.parseTextCmdSrc(input, src);
    }

    public Text parsePlaceholders(Text input, ServerPlayerEntity src)
    {
        return PlaceholderParser.parseTextPlayer(input, src);
    }

    public Text parsePlaceholders(Text input, MinecraftServer src)
    {
        return PlaceholderParser.parseTextServer(input, src);
    }

    public Text parsePlaceholders(Text input, Entity src)
    {
        return PlaceholderParser.parseTextEntity(input, src);
    }
}
