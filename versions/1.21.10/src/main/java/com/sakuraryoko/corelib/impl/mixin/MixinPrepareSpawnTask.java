/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.config.PrepareSpawnTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.corelib.impl.events.players.PlayerEventsManager;

@Mixin(PrepareSpawnTask.class)
public class MixinPrepareSpawnTask
{
	@Inject(method = "spawnPlayer", at = @At("RETURN"))
	private void corelib$onSpawnTask(Connection connection, CommonListenerCookie commonListenerCookie, CallbackInfoReturnable<ServerPlayer> cir)
	{
		ServerPlayer serverPlayer = cir.getReturnValue();

		((PlayerEventsManager) PlayerEventsManager.getInstance()).onCreatePlayer(serverPlayer, serverPlayer.getGameProfile());
	}
}
