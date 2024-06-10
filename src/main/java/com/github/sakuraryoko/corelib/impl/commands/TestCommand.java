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

package com.github.sakuraryoko.corelib.impl.commands;

import com.github.sakuraryoko.corelib.impl.network.TestSuite;
import com.github.sakuraryoko.corelib.util.CoreLog;
//#if MC >= 11902
//$$ import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//#else
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
//#endif

import org.jetbrains.annotations.ApiStatus;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@ApiStatus.Internal
public class TestCommand
{
    public static void register()
    {
    //#if MC >= 11902
        //$$ CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
    //#else
        CommandRegistrationCallback.EVENT.register((dispatcher, environment) -> dispatcher.register(
    //#endif
                literal("network-test")
                        .then(literal("client")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> testPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), "", ctx))
                                        .then(argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> testPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), StringArgumentType.getString(ctx, "message"), ctx))
                                        )
                                )
                        )
                        .then(literal("server")
                                .executes(ctx -> testServer(ctx.getSource(), ctx))
                        )
        ));
    }

    private static int testPlayer(ServerCommandSource src, ServerPlayerEntity target, String message, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
    {
        //#if MC >= 12004
        //$$ String user = src.getPlayerOrThrow().getName().getLiteralString();
        //#else
        String user = src.getName();
        //#endif
        String reponse = !message.isEmpty() ? message : "random message";
        if (target != null)
        {
            // Run S2C test -> Player
            TestSuite.testS2C(target, reponse);
        }
        // Run C2S test
        //TestSuite.testC2S(reponse);
        CoreLog.debug("testPlayer(): --> Executed!");
        return 1;
    }
    private static int testServer(ServerCommandSource src, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
    {
        //#if MC >= 12004
        //$$ String user = src.getPlayerOrThrow().getName().getLiteralString();
        //#else
        String user = src.getName();
        //#endif
        // Run C2S test
        TestSuite.testC2S("Random server message");

        //TestSuite.testS2C(src.getPlayerOrThrow(), "Random server message");

        CoreLog.debug("testServer(): --> Executed!");
        return 1;
    }
}
