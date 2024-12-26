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

package com.sakuraryoko.corelib.api.commands;

import com.mojang.brigadier.CommandDispatcher;
//#if MC >= 11902
//$$ import net.minecraft.commands.CommandBuildContext;
//$$ import net.minecraft.commands.Commands;
//#endif
import net.minecraft.commands.CommandSourceStack;

public interface IServerCommand
{
    /**
     * Register a Server Side command, in either Dedicated, or Integrated Servers
     */
//#if MC >= 11902
    //$$ void register(CommandDispatcher<CommandSourceStack> dispatcher,
                  //$$ CommandBuildContext registryAccess,
                  //$$ Commands.CommandSelection environment);
//#else
void register(CommandDispatcher<CommandSourceStack> dispatcher);
//#endif

    String getName();

    String getModId();

    default String getNode()
    {
        return this.getModId() + "." + this.getName();
    }
}
