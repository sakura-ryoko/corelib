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

package com.github.sakuraryoko.corelib.api.events;

import javax.annotation.Nullable;
import net.minecraft.client.world.ClientWorld;

public interface IClientEventsDispatch
{
    void worldChangePre(@Nullable ClientWorld world);

    void worldChangePost(@Nullable ClientWorld world);

    void onJoining(@Nullable ClientWorld world);

    void onOpenConnection(@Nullable ClientWorld world);

    void onJoined(@Nullable ClientWorld world);

    void onGameJoinPre(@Nullable ClientWorld lastWorld);

    void onGameJoinPost(@Nullable ClientWorld newWorld);

    void onDimensionChangePre(@Nullable ClientWorld world);

    void onDimensionChangePost(@Nullable ClientWorld world);

    void onDisconnecting(@Nullable ClientWorld world);

    void onCloseConnection(@Nullable ClientWorld world);

    void onDisconnected(@Nullable ClientWorld world);
}
