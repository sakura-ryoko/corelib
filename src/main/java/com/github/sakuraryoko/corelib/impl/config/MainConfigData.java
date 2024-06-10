package com.github.sakuraryoko.corelib.impl.config;

import com.google.gson.annotations.SerializedName;
import com.github.sakuraryoko.corelib.api.config.IConfigData;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MainConfigData implements IConfigData
{
    @SerializedName("__comment")
    public String comment = "Sakura's Core Library API";

    @SerializedName("config_date")
    public String config_date;
}
