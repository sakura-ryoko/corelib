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

package com.sakuraryoko.corelib.impl.commands;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

import com.mojang.brigadier.CommandDispatcher;
//#if MC >= 11902
//$$ import net.minecraft.commands.CommandBuildContext;
//$$ import net.minecraft.commands.Commands;
//#else
//#endif
import net.minecraft.commands.CommandSourceStack;

import com.sakuraryoko.corelib.api.commands.IServerCommand;

public class CommandManager implements ICommandManager
{
    private static final CommandManager INSTANCE = new CommandManager();
    public static ICommandManager getInstance() { return INSTANCE; }
    private final List<IServerCommand> COMMAND = new ArrayList<>();

    @Override
    public void registerCommandHandler(IServerCommand command) throws RuntimeException
    {
        if (!this.COMMAND.contains(command))
        {
            this.COMMAND.add(command);
        }
        else
        {
            throw new RuntimeException("Command has already been registered!");
        }
    }

    @ApiStatus.Internal
    //#if MC >= 11902
    //$$ public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                 //$$ CommandBuildContext registryAccess,
                                 //$$ Commands.CommandSelection environment)
    //$$ {
        //$$ this.COMMAND.forEach((iServerCommand -> iServerCommand.register(dispatcher, registryAccess, environment)));
    //$$ }
    //#else
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        this.COMMAND.forEach((iServerCommand -> iServerCommand.register(dispatcher)));
    }
    //#endif
}
