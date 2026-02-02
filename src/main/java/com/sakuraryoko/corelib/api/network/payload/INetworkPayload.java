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

package com.sakuraryoko.corelib.api.network.payload;

//#if MC >= 1.20.6
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * A common interface for defining network payloads
 *
 * @param <B>           Extends {@link FriendlyByteBuf}
 * @param <PAYLOAD>     Extends {@link CustomPacketPayload}
 */
//#if MC >= 1.20.6
//$$ public interface INetworkPayload<B extends FriendlyByteBuf, PAYLOAD extends CustomPacketPayload>
//#else
public interface INetworkPayload<B extends FriendlyByteBuf, PAYLOAD>
//#endif
{
	ResourceLocation getPacketId();

	//#if MC >= 1.20.6
	//$$ CustomPacketPayload.TypeAndCodec<B, PAYLOAD> getTypeAndCodec();
	//#else
	PAYLOAD fromByteBuf(B buffer);
	//#endif
}
