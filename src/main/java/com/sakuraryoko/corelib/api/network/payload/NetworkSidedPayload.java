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

import net.minecraft.network.FriendlyByteBuf;
//#if MC >= 1.20.6
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif

//#if MC >= 1.20.6
//$$ public class NetworkSidedPayload<B extends FriendlyByteBuf, PAYLOAD extends CustomPacketPayload>
//#else
public class NetworkSidedPayload<B extends FriendlyByteBuf, PAYLOAD>
//#endif
{
	private final INetworkPayload<B, PAYLOAD> payload;
	private final NetworkSide side;

	public NetworkSidedPayload(NetworkSide side, INetworkPayload<B, PAYLOAD> payload)
	{
		this.side = side;
		this.payload = payload;
	}

	public NetworkSide getSide()
	{
		return this.side;
	}

	public INetworkPayload<B, PAYLOAD> getPayload()
	{
		return this.payload;
	}
}
