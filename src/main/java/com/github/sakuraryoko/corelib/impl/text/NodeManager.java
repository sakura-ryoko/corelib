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

import java.util.Iterator;
import java.util.List;
import com.github.sakuraryoko.corelib.util.CoreLog;
//#if MC >= 11902
//$$ import eu.pb4.placeholders.api.node.TextNode;
//$$ import eu.pb4.placeholders.api.node.parent.ColorNode;
//$$ import eu.pb4.placeholders.api.parsers.TextParserV1;
//$$ import eu.pb4.placeholders.impl.textparser.TextParserImpl;
//#else
import eu.pb4.placeholders.TextParser;
import eu.pb4.placeholders.util.TextParserUtils;
//#endif
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

@ApiStatus.Internal
public class NodeManager
{
    public static void registerColors()
    {
        final Iterator<MoreColorNode> iterator = MoreColorNode.COLORS.iterator();
        MoreColorNode iColorNode;

        while (iterator.hasNext())
        {
            iColorNode = iterator.next();
            if (iColorNode.isRegistered())
                continue;

            // DataResult checked at initialization
            TextColor finalIColorNode = iColorNode.getColor();
            if (iColorNode.getAliases() != null)
            {
//#if MC >= 11902
                //$$ TextParserV1.registerDefault(
                        //$$ TextParserV1.TextTag.of(
                                //$$ iColorNode.getName(),
                                //$$ iColorNode.getAliases(),
                                //$$ "color",
                                //$$ true,
                                //$$ wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                        //$$ )
                //$$ );
//#else
                TextParser.TextFormatterHandler iColor = (tag, data, input, handlers, endAt) ->
                {
                    eu.pb4.placeholders.util.GeneralUtils.TextLengthPair out = TextParserUtils.recursiveParsing(input, handlers, endAt);
                    out.text().fillStyle(Style.EMPTY.withColor(finalIColorNode));
                    return out;
                };
                TextParser.register(iColorNode.getName(), iColor);
                List<String> iAliases = iColorNode.getAliases();
                for (String iAlias : iAliases)
                    TextParser.register(iAlias, iColor);
//#endif
                CoreLog.debug("NodeManager.registerColors() -- Registering "+ iColorNode.getName()+" with hexCode: "+iColorNode.getHexCode()+" successful, with aliases of: "+iColorNode.getAliases().toString());
            }
            else
            {
//#if MC >= 11902
                //$$ TextParserV1.registerDefault(
                        //$$ TextParserV1.TextTag.of(
                                //$$ iColorNode.getName(),
                                //$$ List.of(""),
                                //$$ "color",
                                //$$ true,
                                //$$ wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                        //$$ )
                //$$ );
//#else
                TextParser.register(iColorNode.getName(), (tag, data, input, handlers, endAt) -> {
                    eu.pb4.placeholders.util.GeneralUtils.TextLengthPair out = TextParserUtils.recursiveParsing(input, handlers, endAt);
                    out.text().fillStyle(Style.EMPTY.withColor(finalIColorNode));
                    return out;
                });
//#endif
                CoreLog.debug("NodeManager.registerColors() -- Registering "+ iColorNode.getName()+" with hexCode: "+iColorNode.getHexCode()+" successful.");
            }
            iColorNode.registerColor();
        }
    }

    public static void registerNodes()
    {
        registerColors();
    }

    // Copied wrap() from TextTags.java
    //#if MC >= 11902
    //$$ private static TextParserV1.TagNodeBuilder wrap(Wrapper wrapper)
    //$$ {
        //$$ return (tag, data, input, handlers, endAt) ->
        //$$ {
            //$$ var out = TextParserImpl.recursiveParsing(input, handlers, endAt);
            //$$ return new TextParserV1.TagNodeValue(wrapper.wrap(out.nodes(), data), out.length());
        //$$ };
    //$$ }

    //$$ interface Wrapper
    //$$ {
        //$$ TextNode wrap(TextNode[] nodes, String arg);
    //$$ }
    //#endif
}
