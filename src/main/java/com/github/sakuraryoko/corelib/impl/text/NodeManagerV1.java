package com.github.sakuraryoko.corelib.impl.text;

import java.util.Iterator;
import java.util.List;
import com.github.sakuraryoko.corelib.util.CoreLog;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.node.parent.ColorNode;
import eu.pb4.placeholders.api.parsers.TextParserV1;
import eu.pb4.placeholders.impl.textparser.TextParserImpl;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.text.TextColor;

@ApiStatus.Internal
public class NodeManagerV1
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
                TextParserV1.registerDefault(
                        TextParserV1.TextTag.of(
                                iColorNode.getName(),
                                iColorNode.getAliases(),
                                "color",
                                true,
                                wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                        )
                );
                CoreLog.debug("NodeManagerV1.registerColors() -- Registering "+ iColorNode.getName()+" with hexCode: "+iColorNode.getHexCode()+" successful, with aliases of: "+iColorNode.getAliases().toString());
            }
            else
            {
                TextParserV1.registerDefault(
                        TextParserV1.TextTag.of(
                                iColorNode.getName(),
                                List.of(""),
                                "color",
                                true,
                                wrap((nodes, arg) -> new ColorNode(nodes, finalIColorNode))
                        )
                );
                CoreLog.debug("NodeManagerV1.registerColors() -- Registering "+ iColorNode.getName()+" with hexCode: "+iColorNode.getHexCode()+" successful.");
            }
            iColorNode.registerColor();
        }
    }

    public static void registerNodes()
    {
        registerColors();
    }

    // Copied wrap() from TextTags.java
    private static TextParserV1.TagNodeBuilder wrap(Wrapper wrapper)
    {
        return (tag, data, input, handlers, endAt) ->
        {
            var out = TextParserImpl.recursiveParsing(input, handlers, endAt);
            return new TextParserV1.TagNodeValue(wrapper.wrap(out.nodes(), data), out.length());
        };
    }

    interface Wrapper
    {
        TextNode wrap(TextNode[] nodes, String arg);
    }
}
