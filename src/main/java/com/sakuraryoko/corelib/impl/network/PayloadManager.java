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

package com.sakuraryoko.corelib.impl.network;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.FriendlyByteBuf;
//#if MC >= 1.20.6
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif
import net.minecraft.resources.ResourceLocation;

import com.sakuraryoko.corelib.api.network.payload.NetworkSide;
import com.sakuraryoko.corelib.api.network.payload.NetworkSidedPayload;

/**
 * Payload Manager that store's the {@link NetworkSidedPayload} references
 * that is primarily for Payload registration in later (1.20.6+) versions.
 */
public class PayloadManager implements IPayloadManager
{
	private static final PayloadManager INSTANCE = new PayloadManager();
	public static PayloadManager getInstance() { return INSTANCE; }
	private final List<NetworkSidedPayload<?, ?>> payloads = new ArrayList<>();

	@Override
	public void registerPayload(NetworkSidedPayload<?, ?> sidedPayload)
	{
		this.payloads.add(sidedPayload);
	}

	//#if MC >= 1.20.6
	//$$ @ApiStatus.Internal
	//$$ @SuppressWarnings("unchecked")
	//$$ public <B extends FriendlyByteBuf, P extends CustomPacketPayload> List<CustomPacketPayload.TypeAndCodec<B, P>> onRegisterC2SPayloads()
	//$$ {
		//$$ List<CustomPacketPayload.TypeAndCodec<B, P>> list = new ArrayList<>();

		//$$ for (NetworkSidedPayload<?, ?> entry : this.payloads)
		//$$ {
			//$$ if (entry.getSide() == NetworkSide.C2S)
			//$$ {
				//$$ list.add((CustomPacketPayload.TypeAndCodec<B, P>) entry.getPayload().getTypeAndCodec());
			//$$ }
		//$$ }

		//$$ return list;
	//$$ }

	//$$ @ApiStatus.Internal
	//$$ @SuppressWarnings("unchecked")
	//$$ public <B extends FriendlyByteBuf, P extends CustomPacketPayload> List<CustomPacketPayload.TypeAndCodec<B, P>> onRegisterS2CPayloads()
	//$$ {
		//$$ List<CustomPacketPayload.TypeAndCodec<B, P>> list = new ArrayList<>();

		//$$ for (NetworkSidedPayload<?, ?> entry : this.payloads)
		//$$ {
			//$$ if (entry.getSide() == NetworkSide.S2C)
			//$$ {
				//$$ list.add((CustomPacketPayload.TypeAndCodec<B, P>) entry.getPayload().getTypeAndCodec());
			//$$ }
		//$$ }

		//$$ return list;
	//$$ }
	//#else
	// Not really needed, but I'll just leave it here for now.
	@ApiStatus.Internal
	public List<ResourceLocation> onRegisterC2SPayloads()
	{
		List<ResourceLocation> list = new ArrayList<>();

		for (NetworkSidedPayload<?, ?> entry : this.payloads)
		{
			if (entry.getSide() == NetworkSide.C2S)
			{
				list.add(entry.getPayload().getPacketId());
			}
		}

		return list;
	}

	// Not really needed, but I'll just leave it here for now.
	@ApiStatus.Internal
	public List<ResourceLocation> onRegisterS2CPayloads()
	{
		List<ResourceLocation> list = new ArrayList<>();

		for (NetworkSidedPayload<?, ?> entry : this.payloads)
		{
			if (entry.getSide() == NetworkSide.S2C)
			{
				list.add(entry.getPayload().getPacketId());
			}
		}

		return list;
	}

	// Primarily utilized by 1.20.2 - 1.20.4 Only.
	@ApiStatus.Internal
	public <B extends FriendlyByteBuf, P> P fromByteBuf(ResourceLocation id, B buf)
	{
		for (NetworkSidedPayload<?, ?> entry : this.payloads)
		{
			if (entry.getPayload().getPacketId().equals(id))
			{
				@SuppressWarnings("unchecked")
				NetworkSidedPayload<B, P> ent = (NetworkSidedPayload<B, P>) entry;
				return ent.getPayload().fromByteBuf(buf);
			}
		}

		return null;
	}
	//#endif
}
