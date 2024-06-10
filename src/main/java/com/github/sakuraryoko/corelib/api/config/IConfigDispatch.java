package com.github.sakuraryoko.corelib.api.config;

public interface IConfigDispatch
{
    IConfigData newConfig();
    IConfigData getConfig();
    boolean isLoaded();
    void onPreLoadConfig();
    void onPostLoadConfig();
    void onPreSaveConfig();
    void onPostSaveConfig();
    IConfigData defaults();
    IConfigData update(IConfigData data);
    void execute();
}
