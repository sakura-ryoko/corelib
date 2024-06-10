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
