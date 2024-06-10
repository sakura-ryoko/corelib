package com.github.sakuraryoko.corelib.api.network.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.CustomPayload;

@Environment(EnvType.CLIENT)
public interface IClientPlayHandler
{
    <P extends CustomPayload> void registerClientPlayHandler(IPluginClientPlayHandler<P> handler);
    <P extends CustomPayload> void unregisterClientPlayHandler(IPluginClientPlayHandler<P> handler);
}
