package com.github.sakuraryoko.corelib.api.network.server;

import com.google.common.collect.ArrayListMultimap;
import com.github.sakuraryoko.corelib.api.network.CoreBuf;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * The Server Network Play handler
 * @param <T> (Payload)
 */
public class ServerPlayHandler<T extends CustomPayload> implements IServerPlayHandler
{
    private static final ServerPlayHandler<CustomPayload> INSTANCE = new ServerPlayHandler<>();
    private final ArrayListMultimap<Identifier, IPluginServerPlayHandler<T>> handlers = ArrayListMultimap.create();
    public static IServerPlayHandler getInstance()
    {
        return INSTANCE;
    }

    private ServerPlayHandler() {}

    @Override
    @SuppressWarnings("unchecked")
    public <P extends CustomPayload> void registerServerPlayHandler(IPluginServerPlayHandler<P> handler)
    {
        Identifier channel = handler.getPayloadChannel();

        if (!this.handlers.containsEntry(channel, handler))
        {
            this.handlers.put(channel, (IPluginServerPlayHandler<T>) handler);
        }
    }

    @Override
    public <P extends CustomPayload> void unregisterServerPlayHandler(IPluginServerPlayHandler<P> handler)
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
    public void decodeNbtCompound(Identifier channel, ServerPlayerEntity player, NbtCompound data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeNbtCompound(channel, player, data));
        }
    }

    @ApiStatus.Internal
    public void decodeByteBuf(Identifier channel, ServerPlayerEntity player, CoreBuf data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeByteBuf(channel, player, data));
        }
    }

    @ApiStatus.Internal
    public <D> void decodeObject(Identifier channel, ServerPlayerEntity player, D data)
    {
        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((id, handler) -> handler.decodeObject(channel, player, data));
        }
    }
}
