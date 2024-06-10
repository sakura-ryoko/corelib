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
