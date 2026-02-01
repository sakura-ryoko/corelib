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

package com.sakuraryoko.corelib.impl;

import java.nio.file.Path;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.SharedConstants;

@ApiStatus.Internal
public class Reference
{
	public static final Path GAME_DIR = FabricReference.GAME_DIR;
    public static final Path CONFIG_DIR = FabricReference.CONFIG_DIR;
    public static final String MOD_ID = "corelib";
	//#if MC >= 12106
	//$$ public static final String MC_VERSION = SharedConstants.getCurrentVersion().name();
	//#else
	public static final String MC_VERSION = SharedConstants.getCurrentVersion().getName();
	//#endif

	private static final boolean LOCAL_DEBUG = false;
	private static final boolean LOCAL_ANSI_COLOR = false;
	public static final boolean EXPERIMENTAL = false;

    public static boolean DEBUG = isDebug();
	public static boolean RUNNING_IN_IDE = FabricReference.RUNNING_IN_IDE;
	public static boolean ANSI_COLOR = isAnsiColor();

	private static boolean isDebug()
	{
		return LOCAL_DEBUG || RUNNING_IN_IDE;
	}

	private static boolean isAnsiColor()
	{
		return (DEBUG && RUNNING_IN_IDE) || LOCAL_ANSI_COLOR;
	}
}
