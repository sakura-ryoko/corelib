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

import java.util.HashMap;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.sakuraryoko.corelib.api.network.listener.IClientPacketListener;
import com.sakuraryoko.corelib.api.network.listener.IServerPacketListener;

/**
 * Packet Listener manager that handles the Network IO (Send/Receive) for each Channel ID;
 * and then forwards the calls to the appropriate {@link IServerPacketListener} or the {@link IClientPacketListener}
 * depending on the {@link com.sakuraryoko.corelib.api.network.payload.NetworkSide} (S2C/C2S) that is utilized.
 */
public class PacketListenerManager implements IPacketListenerManager
{
	private static final PacketListenerManager INSTANCE = new PacketListenerManager();
	public static PacketListenerManager getInstance() { return INSTANCE; }
	private final HashMap<ResourceLocation, IClientPacketListener<?, ?, ?>> clientListeners = new HashMap<>();
	private final HashMap<ResourceLocation, IServerPacketListener<?, ?, ?>> serverListeners = new HashMap<>();

	@Override
	public void registerS2CPacketListener(IClientPacketListener<?, ?, ?> listener)
	{
		if (!this.clientListeners.containsKey(listener.getPacketId()))
		{
			this.clientListeners.put(listener.getPacketId(), listener);
		}
	}

	@Override
	public void registerC2SPacketListener(IServerPacketListener<?, ?, ?> listener)
	{
		if (!this.serverListeners.containsKey(listener.getPacketId()))
		{
			this.serverListeners.put(listener.getPacketId(), listener);
		}
	}

	@Override
	public void unregisterS2CPacketListener(IClientPacketListener<?, ?, ?> listener)
	{
		this.clientListeners.remove(listener.getPacketId());
	}

	@Override
	public void unregisterC2SPacketListener(IServerPacketListener<?, ?, ?> listener)
	{
		this.serverListeners.remove(listener.getPacketId());
	}

	public Set<ResourceLocation> getS2CKeySet()
	{
		return this.clientListeners.keySet();
	}

	public Set<ResourceLocation> getC2SKeySet()
	{
		return this.serverListeners.keySet();
	}

	public <T extends PacketListener> boolean sendC2SPacket(ResourceLocation id, Packet<T> packet)
	{
		if (this.clientListeners.containsKey(id))
		{
			this.clientListeners.get(id).sendPacket(packet);
			return true;
		}

		return false;
	}

	public <T extends PacketListener> boolean sendS2CPacket(ResourceLocation id, Packet<T> packet, ServerPlayer player)
	{
		if (this.serverListeners.containsKey(id))
		{
			this.serverListeners.get(id).sendPacket(packet, player);
			return true;
		}

		return false;
	}

	@ApiStatus.Internal
	public <T extends PacketListener> boolean onS2CPacketReceived(ResourceLocation id, Packet<T> packet)
	{
		if (this.clientListeners.containsKey(id))
		{
			return this.clientListeners.get(id).receivePacket(packet);
		}

		return false;
	}

	@ApiStatus.Internal
	public <T extends PacketListener> boolean onC2SPacketReceived(ResourceLocation id, Packet<T> packet, ServerPlayer player)
	{
		if (this.serverListeners.containsKey(id))
		{
			return this.serverListeners.get(id).receivePacket(packet, player);
		}

		return false;
	}
}
