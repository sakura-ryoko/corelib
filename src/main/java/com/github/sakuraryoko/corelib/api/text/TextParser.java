package com.github.sakuraryoko.corelib.api.text;

import com.github.sakuraryoko.corelib.impl.text.NodeParserV1;
import com.github.sakuraryoko.corelib.impl.text.PlaceholderParserV1;
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
        return NodeParserV1.parse(input, true);
    }

    public Text parseNodes(String input, boolean safe)
    {
        return NodeParserV1.parse(input, safe);
    }

    public Text parsePlaceholders(Text input, ServerCommandSource src)
    {
        return PlaceholderParserV1.parseTextCmdSrc(input, src);
    }

    public Text parsePlaceholders(Text input, ServerPlayerEntity src)
    {
        return PlaceholderParserV1.parseTextPlayer(input, src);
    }

    public Text parsePlaceholders(Text input, MinecraftServer src)
    {
        return PlaceholderParserV1.parseTextServer(input, src);
    }

    public Text parsePlaceholders(Text input, Entity src)
    {
        return PlaceholderParserV1.parseTextEntity(input, src);
    }
}
