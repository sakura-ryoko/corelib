/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
