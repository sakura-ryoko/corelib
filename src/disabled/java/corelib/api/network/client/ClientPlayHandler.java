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
