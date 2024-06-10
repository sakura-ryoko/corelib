package com.github.sakuraryoko.corelib.util;

import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoreLog
{
    private static final String CORE_ID = CoreInitHandler.getInstance().getModId();
    private static final boolean CORE_DEBUG = CoreInitHandler.getInstance().isDebug();
    private static final boolean CORE_WRAP_ID = CoreInitHandler.getInstance().isWrapID();
    private static final Logger LOGGER = LogManager.getLogger(CORE_ID);

    public static void debug(String msg, Object... args)
    {
        if (CORE_DEBUG)
        {
            final String wrapDebug = CORE_WRAP_ID ? "["+CORE_ID+":DEBUG] "+msg : "[DEBUG] "+msg;
            LOGGER.info(wrapDebug, args);
        }
        else
        {
            LOGGER.debug(wrap(msg), args);
        }
    }

    public static void info(String msg, Object... args)
    {
        LOGGER.info(wrap(msg), args);
    }

    public static void warn(String msg, Object... args)
    {
        LOGGER.warn(wrap(msg), args);
    }

    public static void error(String msg, Object... args)
    {
        LOGGER.error(wrap(msg), args);
    }

    public static void fatal(String msg, Object... args)
    {
        LOGGER.fatal(wrap(msg), args);
    }

    private static String wrap(String msg)
    {
        if (CORE_WRAP_ID)
        {
            return "["+CORE_ID+"] "+msg;
        }

        return msg;
    }
}
