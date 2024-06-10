/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * CoreLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CoreLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CoreLib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sakuraryoko.corelib.impl.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.SerializedName;
import com.github.sakuraryoko.corelib.api.config.IConfigData;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MoreColorConfigData implements IConfigData
{
    @SerializedName("__comment1")
    public String comment1 = "Use colors {} entry list with \"name\": \"#FFFFFF\" to add color nodes.";

    @SerializedName("__comment2")
    public String comment2 = "Use aliases {} entry list to map an existing node to one or more nodes.";

    @SerializedName("__comment3")
    public String comment3 = "- List the existing colors {} \"name\" and then create a new entry list of it's alias tag names.";

    @SerializedName("colors")
    public Map<String, String> COLORS = new HashMap<>();

    @SerializedName("aliases")
    public Map<String, List<String>> ALIASES = new HashMap<>();
}
