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

package com.github.sakuraryoko.corelib.api.network.client;

import com.google.common.collect.ArrayListMultimap;
import com.github.sakuraryoko.corelib.api.network.CoreBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * The Client Network Play handler
 * @param <T> (Payload)
 */
@Environment(EnvType.CLIENT)
public class ClientPlayHandler<T extends CustomPayload> implements IClientPlayHandler
{
    private static final ClientPlayHandler<CustomPayload> INSTANCE = new ClientPlayHandler<>();
    private final ArrayListMultimap<Identifier, IPluginClientPlayHandler<T>> handlers = ArrayListMultimap.create();
    public static IClientPlayHandler getInstance()
    {
        return INSTANCE;
    }

    private ClientPlayHandler() {}

    @Override
    @SuppressWarnings("unchecked")
    public <P extends CustomPayload> void registerClientPlayHandler(IPluginClientPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();

        if (!this.handlers.containsEntry(channel, handler))
        {
            this.handlers.put(channel, (IPluginClientPlayHandler<T>) handler);
        }
    }

    @Override
    public <P extends CustomPayload> void unregisterClientPlayHandler(IPluginClientPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();

        if (this.handlers.remove(channel, handler))
        {
            handler.reset(channel);
            handler.unregisterPlayReceiver();
        }
    }

    @ApiStatus.Internal
    public void reset(Identifier channel)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.reset(channel));
        }
    }

    @ApiStatus.Internal
    public void decodeNbtCompound(Identifier channel, NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeNbtCompound(channel, data));
        }
    }

    @ApiStatus.Internal
    public void decodeByteBuf(Identifier channel, CoreBuf data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeByteBuf(channel, data));
        }
    }

    @ApiStatus.Internal
    public <D> void decodeObject(Identifier channel, D data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeObject(channel, data));
        }
    }
}
