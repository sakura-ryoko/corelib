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

package com.github.sakuraryoko.corelib.impl.commands;

//#if MC >= 12006
//$$ import com.github.sakuraryoko.corelib.impl.network.TestSuite;
//#endif
import com.github.sakuraryoko.corelib.util.CoreLog;
import com.github.sakuraryoko.corelib.util.PlayerNameUtil;
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
        String user = PlayerNameUtil.getPlayerName(src);
        String reponse = !message.isEmpty() ? message : "random message";

        if (target != null)
        {
            // Run S2C test -> Player
            //#if MC >= 12006
            //$$ TestSuite.testS2C(target, reponse);
            //#endif
        }
        // Run C2S test
        //TestSuite.testC2S(reponse);
        CoreLog.debug("testPlayer(): --> Executed!");
        return 1;
    }
    private static int testServer(ServerCommandSource src, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
    {
        String user = PlayerNameUtil.getPlayerName(src);

        // Run C2S test
        //#if MC >= 12006
        //$$ TestSuite.testC2S("Random server message");
        //#endif

        //TestSuite.testS2C(src.getPlayerOrThrow(), "Random server message");

        CoreLog.debug("testServer(): --> Executed!");
        return 1;
    }
}
