package com.github.sakuraryoko.corelib.api.init;

import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

public interface IModInitDispatch
{
    ModInitData getModInit();
    String getModId();
    List<String> getBasic(List<String> elements);
    List<Text> getFormatted(List<String> elements);
    boolean isDebug();
    boolean isWrapID();
    void onModInit();
    default void setIntegratedServer(boolean toggle) { this.getModInit().setIntegratedServer(toggle); }
    default void setDedicatedServer(boolean toggle) { this.getModInit().setDedicatedServer(toggle); }
    default void setOpenToLan(boolean toggle) { this.getModInit().setOpenToLan(toggle); }
    default void reset() { this.getModInit().reset(); }

    default FabricLoader getModInstance() { return this.getModInit().getModInstance(); }
    default boolean isClient() { return this.getModInit().isClient(); }
    default boolean isServer() { return this.getModInit().isServer(); }
    default boolean isIntegratedServer() { return this.getModInit().isIntegratedServer(); }
    default boolean isDedicatedServer() { return this.getModInit().isDedicatedServer(); }
    default boolean isOpenToLan() { return this.getModInit().isOpenToLan(); }
    default String getMcVersion() { return this.getModInit().getMCVersion(); }
    default String getModVersion() { return this.getModInit().getModVersion(); }
    default String getModDesc() { return this.getModInit().getModDesc(); }
    default String getModAuthors() { return this.getModInit().getModAuthor$String(); }
    default String getModSources() { return this.getModInit().getModSources(); }
    default String getModHomepage() { return this.getModInit().getModHomepage(); }
}
