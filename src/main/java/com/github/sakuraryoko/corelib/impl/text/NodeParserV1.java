package com.github.sakuraryoko.corelib.impl.text;

import eu.pb4.placeholders.api.TextParserUtils;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.text.Text;

@ApiStatus.Internal
public class NodeParserV1
{
    public static Text parse(String input)
    {
        return parse(input, true);
    }

    public static Text parse(String input, boolean safe)
    {
        if (safe)
        {
            return TextParserUtils.formatTextSafe(input);
        }

        return TextParserUtils.formatText(input);
    }
}
