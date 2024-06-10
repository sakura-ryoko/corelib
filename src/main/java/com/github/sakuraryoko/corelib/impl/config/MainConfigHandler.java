package com.github.sakuraryoko.corelib.impl.config;

import com.github.sakuraryoko.corelib.api.config.IConfigData;
import com.github.sakuraryoko.corelib.api.config.IConfigDispatch;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.util.Util;

@ApiStatus.Internal
public class MainConfigHandler implements IConfigDispatch
{
    private static final MainConfigHandler INSTANCE = new MainConfigHandler();
    public static MainConfigHandler getInstance() { return INSTANCE; }
    public static MainConfigData CONFIG = new MainConfigData();
    private boolean loaded = false;

    @Override
    public MainConfigData newConfig()
    {
        return new MainConfigData();
    }

    @Override
    public MainConfigData getConfig()
    {
        return CONFIG;
    }

    @Override
    public boolean isLoaded()
    {
        return this.loaded;
    }

    @Override
    public void onPreLoadConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostLoadConfig()
    {
        this.loaded = true;
    }

    @Override
    public void onPreSaveConfig()
    {
        this.loaded = false;
    }

    @Override
    public void onPostSaveConfig()
    {
        this.loaded = true;
    }

    @Override
    public MainConfigData defaults()
    {
        MainConfigData conf = this.newConfig();
        CoreLog.debug("MainConfigHandler.defaults() has been called.");

        conf.config_date = Util.getFormattedCurrentTime();

        return conf;
    }

    @Override
    public MainConfigData update(IConfigData newConfig)
    {
        MainConfigData newConf = (MainConfigData) newConfig;
        CoreLog.debug("MainConfigHandler.refresh() has been called.");

        // Refresh Data from Config
        CONFIG.config_date = Util.getFormattedCurrentTime();
        CoreLog.debug("MainConfigHandler.refresh(): config_date: {} --> {}", newConf.config_date, CONFIG.config_date);

        return CONFIG;
    }

    @Override
    public void execute()
    {
        CoreLog.debug("MainConfigHandler.execute() has been called.");

        // Do this when the Main Config gets reloaded.
        CoreLog.debug("MainConfigHandler.execute(): new config_date: {}", CONFIG.config_date);
    }
}
