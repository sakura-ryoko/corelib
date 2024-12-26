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

package com.sakuraryoko.corelib.api.modinit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.network.chat.Component;
import net.fabricmc.loader.api.FabricLoader;

import com.sakuraryoko.corelib.api.text.ITextHandler;

public interface IModInitDispatcher
{
    ModInitData getModInit();

    String getModId();

    ITextHandler getTextHandler();

    default List<String> getBasic(List<String> elements)
    {
        Map<String, String> infoBasic = this.getModInit().getModBasicInfo();
        List<String> result = new ArrayList<>();

        for (String element : elements)
        {
            if (infoBasic.containsKey(element))
            {
                result.add(infoBasic.get(element));
            }
        }

        return result;
    }

    default List<Component> getFormatted(List<String> elements)
    {
        Map<String, Component> infoFmt = this.getModInit().getModFormattedInfo();
        List<Component> result = new ArrayList<>();

        for (String element : elements)
        {
            if (infoFmt.containsKey(element))
            {
                result.add(infoFmt.get(element));
            }
        }

        return result;
    }

    boolean isDebug();

    boolean isInitComplete();

    void onModInit();

    default void setIntegratedServer(boolean toggle) {this.getModInit().setIntegratedServer(toggle);}

    default void setDedicatedServer(boolean toggle) {this.getModInit().setDedicatedServer(toggle);}

    default void setOpenToLan(boolean toggle) {this.getModInit().setOpenToLan(toggle);}

    default void reset() {this.getModInit().reset();}

    default FabricLoader getModInstance() {return this.getModInit().getModInstance();}

    default boolean isClient() {return this.getModInit().isClient();}

    default boolean isServer() {return this.getModInit().isServer();}

    default boolean isIntegratedServer() {return this.getModInit().isIntegratedServer();}

    default boolean isDedicatedServer() {return this.getModInit().isDedicatedServer();}

    default boolean isOpenToLan() {return this.getModInit().isOpenToLan();}

    default String getMcVersion() {return this.getModInit().getMCVersion();}

    default String getModVersion() {return this.getModInit().getModVersion();}

    default String getModName() {return this.getModInit().getModName();}

    default String getModVersionString()
    {
        return this.getModName() + "-" + this.getMcVersion() + "-" + this.getModVersion();
    }

    default String getModDesc() {return this.getModInit().getModDesc();}

    default String getModAuthors() {return this.getModInit().getModAuthor$String();}

    default String getModSources() {return this.getModInit().getModSources();}

    default String getModHomepage() {return this.getModInit().getModHomepage();}
}
