/*
 * This file is part of the CoreLib project, licensed under the
 * MIT License
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
