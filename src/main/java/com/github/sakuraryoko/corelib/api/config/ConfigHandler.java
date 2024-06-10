package com.github.sakuraryoko.corelib.api.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.github.sakuraryoko.corelib.impl.init.CoreInitHandler;
import com.github.sakuraryoko.corelib.util.CoreLog;
import org.jetbrains.annotations.ApiStatus;

public class ConfigHandler implements IConfigManager
{
    private static final ConfigHandler INSTANCE = new ConfigHandler();
    private final Map<String, ConfigHandlerObject> handlers = new HashMap<>();
    private final Map<String, Boolean> isLoaded = new HashMap<>();
    public static IConfigManager getInstance() { return INSTANCE; }
    private final Path CONFIG_DIR = CoreInitHandler.getInstance().getModInstance().getConfigDir();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient()
            .create();

    @Override
    public void registerConfigHandler(ConfigHandlerObject object)
    {
        if (!this.handlers.containsKey(object.getKey()))
        {
            CoreLog.debug("registerModConfigHandler: [{}]", object.getKey());
            this.handlers.put(object.getKey(), object);
            this.isLoaded.put(object.getKey(), false);
        }
    }

    /**
     * Allowed to be used to reload a specific config file
     */
    @Override
    public boolean reloadConfig(ConfigHandlerObject obj)
    {
        CoreLog.debug("reloadConfig() for key [{}]", obj.getKey());

        if (this.handlers.containsKey(obj.getKey()))
        {
            this.loadEach(obj);

            return true;
        }

        return false;
    }

    /**
     * Allowed to be used to see if a specific config file is marked loaded
     */
    @Override
    public boolean isLoaded(ConfigHandlerObject obj)
    {
        CoreLog.debug("isLoaded() for key [{}]", obj.getKey());

        if (this.handlers.containsKey(obj.getKey()))
        {
            return this.isLoaded.get(obj.getKey());
        }

        return false;
    }

    @ApiStatus.Internal
    public void loadAllConfigs()
    {
        CoreLog.debug("loadAllConfigs()");

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((key, object) ->
            {
                if (!object.getHandler().isLoaded())
                {
                    object.getHandler().onPreLoadConfig();
                    this.loadEach(object);
                    object.getHandler().onPostLoadConfig();
                }
            });
        }
    }

    @ApiStatus.Internal
    public void saveAllConfigs()
    {
        CoreLog.debug("saveAllConfigs()");

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((key, object) ->
            {
                if (object.getHandler().isLoaded())
                {
                    object.getHandler().onPreSaveConfig();
                    this.saveEach(object);
                    object.getHandler().onPostSaveConfig();
                }
                else
                {
                    object.getHandler().onPreLoadConfig();
                    this.loadEach(object);
                    object.getHandler().onPostLoadConfig();
                }
            });
        }
    }

    @ApiStatus.Internal
    public void defaultsAll()
    {
        CoreLog.debug("defaultsAll()");

        if (!this.handlers.isEmpty())
        {
            this.handlers.forEach((key, object) ->
            {
                IConfigData conf;
                conf = object.getHandler().defaults();
                object.getHandler().update(conf);
                object.getHandler().execute();
            });
        }
    }

    @ApiStatus.Internal
    private void loadEach(ConfigHandlerObject object)
    {
        IConfigData conf = object.getHandler().newConfig();

        this.isLoaded.put(object.getKey(), false);
        CoreLog.debug("loadEach() for key [{}]", object.getKey());

        try
        {
            Path configDir;

            if (object.getRoorDir())
            {
                configDir = CONFIG_DIR;
            }
            else
            {
                configDir = CONFIG_DIR.resolve(object.getModid());
            }

            if (!Files.isDirectory(configDir))
            {
                Files.createDirectory(configDir);
            }

            var configFile = configDir.resolve(object.getConfigFile() + ".json");

            if (Files.exists(configFile))
            {
                var getData = JsonParser.parseString(Files.readString(configFile));
                conf = GSON.fromJson(getData, conf.getClass());
            }
            else
            {
                conf = object.getHandler().defaults();
            }

            conf = object.getHandler().update(conf);
            Files.writeString(configFile, GSON.toJson(conf));
            object.getHandler().execute();

            this.isLoaded.put(object.getKey(), true);
        }
        catch (Exception e)
        {
            this.isLoaded.put(object.getKey(), false);
            CoreLog.error("Error reading config file for key [{}] // {}", object.getKey(), e.getMessage());
        }
    }

    @ApiStatus.Internal
    private void saveEach(ConfigHandlerObject object)
    {
        IConfigData conf;

        this.isLoaded.put(object.getModid(), false);
        CoreLog.debug("saveEach() for key [{}]", object.getKey());

        try
        {
            Path configDir;

            if (object.getRoorDir())
            {
                configDir = CONFIG_DIR;
            }
            else
            {
                configDir = CONFIG_DIR.resolve(object.getModid());
            }

            if (!Files.isDirectory(configDir))
            {
                Files.createDirectory(configDir);
            }

            var configFile = configDir.resolve(object.getConfigFile() + ".json");

            if (Files.exists(configFile))
            {
                Files.delete(configFile);
            }

            conf = object.getHandler().getConfig();

            if (conf != null)
            {
                Files.writeString(configFile, GSON.toJson(conf));
                this.isLoaded.put(object.getKey(), true);
            }
            else
            {
                this.isLoaded.put(object.getKey(), false);
                CoreLog.error("Error saving config file for key [{}] // config object is empty", object.getKey());
            }
        }
        catch (Exception e)
        {
            this.isLoaded.put(object.getKey(), false);
            CoreLog.error("Error saving config file for key [{}] // {}", object.getKey(), e.getMessage());
        }
    }
}
