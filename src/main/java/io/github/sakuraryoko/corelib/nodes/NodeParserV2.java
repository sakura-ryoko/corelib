package io.github.sakuraryoko.corelib.nodes;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;

public class NodeParserV2 {
    public static Text parse(String input) { return TextParserUtils.formatTextSafe(input); }
}
