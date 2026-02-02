/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.mixin.network.c2s;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.sakuraryoko.corelib.impl.network.PayloadManager;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket_customPayload
{
	@Redirect(method = "<clinit>",
	          at = @At(value = "INVOKE",
	                 target = "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;"))
	@SuppressWarnings("unchecked")
	private static <E, B extends FriendlyByteBuf, P extends CustomPacketPayload> ArrayList<E> corelib$injectC2SPayloadTypes(E[] elements)
	{
		// Copy and replace if and only if not found
		ArrayList<E> list = new ArrayList<>(Arrays.asList(elements));
		ArrayList<E> newList = new ArrayList<>();

		List<CustomPacketPayload.TypeAndCodec<B, P>> toRegister = PayloadManager.getInstance().onRegisterC2SPayloads();
		List<CustomPacketPayload.TypeAndCodec<B, P>> notFound = new ArrayList<>(toRegister);
		boolean updated = false;

		for (E e : list)
		{
			CustomPacketPayload.Type<?> type = ((CustomPacketPayload.TypeAndCodec<B, P>) e).type();

			for (CustomPacketPayload.TypeAndCodec<B, P> entry : toRegister)
			{
				if (type.equals(entry.type()))
				{
					notFound.remove(entry);
				}
			}

			newList.add(e);
		}

		if (!notFound.isEmpty())
		{
			for (CustomPacketPayload.TypeAndCodec<B, P> entry : notFound)
			{
				newList.add((E) entry);
			}

			updated = true;
		}

		if (updated)
		{
			return newList;
		}

		return list;
	}
}
