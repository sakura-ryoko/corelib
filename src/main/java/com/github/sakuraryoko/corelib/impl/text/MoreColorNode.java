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

package com.github.sakuraryoko.corelib.impl.text;

import java.util.*;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
//#if MC >= 12004
//$$ import com.mojang.serialization.DataResult;
//#endif
import net.minecraft.text.TextColor;

@ApiStatus.Internal
public class MoreColorNode
{
    private final String name;
    private final String hexCode;
    private List<String> aliases;
    private TextColor color;
    private boolean registered;
    private static final Map<String, MoreColorNode> colorMap = new HashMap<>();
    public static final List<MoreColorNode> COLORS = new ArrayList<>();

    public MoreColorNode(String name, String hexCode)
    {
        //#if MC >= 12004
        //$$ DataResult<TextColor> dr = TextColor.parse(hexCode);
        //$$ if (dr.error().isEmpty())
        //$$ {
            //$$ this.name = name;
            //$$ this.hexCode = hexCode;
            //$$ this.color = dr.result().orElse(null);
            //$$ this.registered = false;
            //$$ this.aliases = null;
        //$$ }
        //$$ else
        //$$ {
            //$$ CoreLog.warn("MoreColorNode("+ name +") is Invalid, error: "+dr.error().toString());
            //$$ this.name = "";
            //$$ this.hexCode = "";
        //$$ }
        //#else
        this.color = TextColor.parse(hexCode);

        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
        }
        else
        {
            CoreLog.warn("MoreColorNode("+ name +") unhandled error (color is null)");
            this.name = "";
            this.hexCode = "";
        }
        //#endif
    }

    public MoreColorNode(String name, String hexCode, @Nullable List<String> aliases)
    {
        //#if MC >= 12004
        //$$ DataResult<TextColor> dr = TextColor.parse(hexCode);

        //$$ if (dr.error().isEmpty())
        //$$ {
            //$$ this.name = name;
            //$$ this.hexCode = hexCode;
            //$$ this.color = dr.result().orElse(null);
            //$$ this.aliases = aliases;
            //$$ this.registered = false;
        //$$ }
        //$$ else
        //$$ {
            //$$ CoreLog.warn("MoreColorNode("+ name +") is Invalid, error: "+dr.error().toString());
            //$$ this.name = "";
            //$$ this.hexCode = "";
        //$$ }
        //#else
        this.color = TextColor.parse(hexCode);

        if (this.color != null)
        {
            this.name = name;
            this.hexCode = hexCode;
            this.aliases = aliases;
            this.registered = false;
        }
        else
        {
            CoreLog.warn("MoreColorNode("+ name +") unhandled error (color is null)");
            this.name = "";
            this.hexCode = "";
        }
        //#endif
    }

    public String getName() { return this.name; }

    public String getHexCode() { return this.hexCode; }

    @Nullable
    public List<String> getAliases()
    {
        if (this.aliases == null)
        {
            return null;
        }
        else
        {
            return this.aliases;
        }
    }

    @Nullable
    public TextColor getColor() { return this.color; }

    public static void addColor(String name, String hexCode)
    {
        MoreColorNode color = new MoreColorNode(name, hexCode);

        if (color.getName().isEmpty())
        {
            CoreLog.warn("MoreColorNode.addColor("+name+") has failed!");
            return;
        }
        if (colorMap.get(name) != null)
        {
            CoreLog.warn("MoreColorNode.addColor("+name+") already exists!");
            return;
        }

        COLORS.add(color);
        colorMap.put(name, color);
        CoreLog.debug("MoreColorNode.addColor("+name+") successfully added.");
    }
    public static void addColor(String name, String hexCode, @Nullable List<String> alias)
    {
        MoreColorNode color = new MoreColorNode(name, hexCode, alias);

        if (color.getName().isEmpty())
        {
            CoreLog.warn("MoreColorNode.addColor(" + name + ") has failed!");
            return;
        }
        if (colorMap.get(name) != null)
        {
            CoreLog.warn("MoreColorNode.addColor("+name+") already exists!");
            return;
        }

        COLORS.add(color);
        colorMap.put(name, color);
        CoreLog.debug("MoreColorNode.addColor("+name+") successfully added.");
    }

    void registerColor() { this.registered = true; }

    @Nullable
    public static MoreColorNode getColorByName(String name) { return colorMap.get(name); }

    @Nullable
    public static MoreColorNode getColorByHex(String hexCode)
    {
        final Iterator<MoreColorNode> iterator = COLORS.iterator();
        MoreColorNode result;

        while (iterator.hasNext())
        {
            result = iterator.next();
            if (Objects.equals(result.hexCode, hexCode))
                return result;
        }

        return null;
    }

    public boolean isRegistered() { return this.registered; }
}
