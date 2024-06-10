package com.github.sakuraryoko.corelib.impl.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.github.sakuraryoko.corelib.api.config.ConfigHandler;
import com.github.sakuraryoko.corelib.api.config.ConfigHandlerObject;
import com.github.sakuraryoko.corelib.api.events.ClientEventsHandler;
import com.github.sakuraryoko.corelib.api.events.ServerEventsHandler;
import com.github.sakuraryoko.corelib.api.init.IModInitDispatch;
import com.github.sakuraryoko.corelib.api.init.ModInitData;
import com.github.sakuraryoko.corelib.impl.config.MainConfigHandler;
import com.github.sakuraryoko.corelib.impl.config.MoreColorConfigHandler;
import com.github.sakuraryoko.corelib.impl.events.CoreClientEvents;
import com.github.sakuraryoko.corelib.impl.events.CoreServerEvents;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.text.Text;

@ApiStatus.Internal
public class CoreInitHandler implements IModInitDispatch
{
    private static final CoreInitHandler INSTANCE = new CoreInitHandler();
    public static CoreInitHandler getInstance() { return INSTANCE; }
    private static final String MOD_ID = "corelib";
    private static final ModInitData MOD_INFO = new ModInitData(MOD_ID);

    MainConfigHandler mainConfigHandler = new MainConfigHandler();
    MoreColorConfigHandler moreColorConfigHandler = new MoreColorConfigHandler();
    public final ConfigHandlerObject MAIN_CONFIG = new ConfigHandlerObject(mainConfigHandler, mainConfigHandler.getConfig(), MOD_ID, "main", false);
    public final ConfigHandlerObject MORE_COLORS = new ConfigHandlerObject(moreColorConfigHandler, moreColorConfigHandler.getConfig(), MOD_ID, "more-colors", false);

    @Override
    public ModInitData getModInit()
    {
        return MOD_INFO;
    }

    @Override
    public String getModId()
    {
        return MOD_ID;
    }

    @Override
    public List<String> getBasic(List<String> elements)
    {
        Map<String, String> infoBasic = MOD_INFO.getModBasicInfo();
        List<String> result = new ArrayList<>();

        for (String element : elements)
        {
            result.add(infoBasic.get(element));
        }

        return result;
    }

    @Override
    public List<Text> getFormatted(List<String> elements)
    {
        Map<String, Text> infoFmt = MOD_INFO.getModFormattedInfo();
        List<Text> result = new ArrayList<>();

        for (String element : elements)
        {
            result.add(infoFmt.get(element));
        }

        return result;
    }

    @Override
    public boolean isDebug()
    {
        return true;
    }

    @Override
    public boolean isWrapID()
    {
        return true;
    }

    @Override
    public void onModInit()
    {
        for (String s : getBasic(List.of("ver", "auth", "desc"))) CoreLog.info(s);

        ConfigHandler.getInstance().registerConfigHandler(MAIN_CONFIG);
        ConfigHandler.getInstance().registerConfigHandler(MORE_COLORS);

        if (this.isClient())
        {
            CoreClientEvents clientHandler = new CoreClientEvents();
            ClientEventsHandler.getInstance().registerClientEvents(clientHandler);
        }

        CoreServerEvents severHandler = new CoreServerEvents();
        ServerEventsHandler.getInstance().registerServerEvents(severHandler);

        CoreLog.debug("Successful initialization.");
    }
}
