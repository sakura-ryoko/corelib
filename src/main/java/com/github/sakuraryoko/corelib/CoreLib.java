package com.github.sakuraryoko.corelib;

import com.github.sakuraryoko.corelib.api.init.ModInitHandler;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import net.fabricmc.api.ModInitializer;

public class CoreLib implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ModInitHandler.getInstance().registerModInitHandler(new CoreInitHandler());
    }
}
