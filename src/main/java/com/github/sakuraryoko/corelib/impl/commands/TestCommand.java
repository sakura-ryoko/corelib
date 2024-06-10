package com.github.sakuraryoko.corelib.impl.commands;

import com.github.sakuraryoko.corelib.impl.network.TestSuite;
import com.github.sakuraryoko.corelib.util.CoreLog;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
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
        String user = src.getPlayerOrThrow().getName().getLiteralString();
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
        String user = src.getPlayerOrThrow().getName().getLiteralString();
        // Run C2S test
        TestSuite.testC2S("Random server message");

        //TestSuite.testS2C(src.getPlayerOrThrow(), "Random server message");

        CoreLog.debug("testServer(): --> Executed!");
        return 1;
    }
}
