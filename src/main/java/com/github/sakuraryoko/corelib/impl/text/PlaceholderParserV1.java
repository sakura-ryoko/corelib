package com.github.sakuraryoko.corelib.impl.text;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@ApiStatus.Internal
public class PlaceholderParserV1
{
    public static Text parseTextCmdSrc(Text input, ServerCommandSource src)
    {
        return Placeholders.parseText(input, PlaceholderContext.of(src));
    }

    public static Text parseTextPlayer(Text input, ServerPlayerEntity src)
    {
        return Placeholders.parseText(input, PlaceholderContext.of(src));
    }

    public static Text parseTextServer(Text input, MinecraftServer src)
    {
        return Placeholders.parseText(input, PlaceholderContext.of(src));
    }

    public static Text parseTextEntity(Text input, Entity src)
    {
        return Placeholders.parseText(input, PlaceholderContext.of(src));
    }
}
