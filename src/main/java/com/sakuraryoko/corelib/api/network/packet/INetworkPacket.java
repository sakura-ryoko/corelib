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

package com.sakuraryoko.corelib.api.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

/**
 * Create an Abstractable Packet interface.
 * In more modern versions, after 1.20.2, the C2S and S2C Payloads could be the same;
 * but can still differ in terms of the {@link com.sakuraryoko.corelib.api.network.payload.NetworkSide} being utilized,
 * so we keep them separate to be multi-version compliant.
 *
 * @param <C2S>     The Server bound reference that extends the {@link com.sakuraryoko.corelib.api.network.payload.INetworkPayload}
 * @param <S2C>     The Client bound reference that extends the {@link com.sakuraryoko.corelib.api.network.payload.INetworkPayload}
 */
public interface INetworkPacket<C2S, S2C>
{
	//#if MC >= 1.20.2
	//$$ void toByteBuf(FriendlyByteBuf buf);
	//#else
	FriendlyByteBuf toByteBuf();
	//#endif

	C2S toC2SPacket();

	S2C toS2CPacket();

	ServerboundCustomPayloadPacket asServerBound();

	ClientboundCustomPayloadPacket asClientBound();

	void fromByteBuf(FriendlyByteBuf buf);

	INetworkPacket<C2S, S2C> fromC2SPacket(C2S packet);

	INetworkPacket<C2S, S2C> fromS2CPacket(S2C packet);
}
