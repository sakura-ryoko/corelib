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

import com.sakuraryoko.corelib.api.network.listener.IClientPacketListener;
import com.sakuraryoko.corelib.api.network.listener.IServerPacketListener;

/**
 * The {@link PacketListenerManager} registration interface
 */
public interface IPacketListenerManager
{
	void registerS2CPacketListener(IClientPacketListener<?, ?, ?> listener);

	void registerC2SPacketListener(IServerPacketListener<?, ?, ?> listener);

	void unregisterS2CPacketListener(IClientPacketListener<?, ?, ?> listener);

	void unregisterC2SPacketListener(IServerPacketListener<?, ?, ?> listener);
}
