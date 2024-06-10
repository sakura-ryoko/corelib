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
