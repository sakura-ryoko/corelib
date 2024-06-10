package com.github.sakuraryoko.corelib.api.config;

public interface IConfigManager
{
    void registerConfigHandler(ConfigHandlerObject object);
    boolean reloadConfig(ConfigHandlerObject obj);
    boolean isLoaded(ConfigHandlerObject obj);
}
