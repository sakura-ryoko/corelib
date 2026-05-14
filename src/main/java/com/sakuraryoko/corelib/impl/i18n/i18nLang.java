/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.i18n;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sakuraryoko.corelib.api.i18n.i18nOption;
import com.sakuraryoko.corelib.impl.CoreLib;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@ApiStatus.Internal
public class i18nLang
{
	private static final Gson GSON = new Gson();
	private final String langCode;
	private ImmutableMap<String, String> map;

	private i18nLang(String langCode)
	{
		this.langCode = langCode;
		this.map = null;
	}

	@Nullable
	protected static i18nLang load(final String dir, final String langCode) throws IOException
	{
		final String filePath = "/"+dir+"/"+langCode+".json";
		InputStream is = i18nLang.class.getResourceAsStream(filePath);
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		i18nLang lang = new i18nLang(langCode);

		try
		{
			if (is != null)
			{
				JsonObject obj = GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);

				for (Map.Entry<String, JsonElement> entry : obj.entrySet())
				{
					builder.put(entry.getKey(), entry.getValue().getAsString());
				}

				lang.map = builder.build();
				is.close();

				CoreLib.LOGGER.info("i18nLang#load: File: '{}' has been loaded successfully", filePath);

				return lang;
			}
			else
			{
				CoreLib.LOGGER.error("i18nLang#load: Error; file not found: '{}'", filePath);
				return null;
			}
		}
		catch (Throwable t)
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (Throwable t1)
				{
					t.addSuppressed(t1);
				}
			}

			throw t;
		}
	}

	public String getLangCode()
	{
		return this.langCode;
	}

	public i18nOption toOption()
	{
		return i18nOption.fromString(this.langCode);
	}

	public boolean hasTranslation(final String key)
	{
		return this.map.containsKey(key);
	}

	public @Nullable String get(final String key)
	{
		return this.map.get(key);
	}

	public String getOrDefault(final String key, final String defaultString)
	{
		return this.map.getOrDefault(key, defaultString);
	}

	//#if MC >= 1.21.0
	//$$ public MutableComponent translate(final String key, Object... args)
	//$$ {
		//$$ if (this.hasTranslation(key))
		//$$ {
			//$$ return Component.translatableWithFallback(key, this.get(key), args);
		//$$ }
		//$$ else
		//$$ {
			//$$ return Component.literal(key)
				//$$ .withStyle((style) ->
					//$$ style.withColor(ChatFormatting.RED)
					//$$ .withHoverEvent(new HoverEvent.ShowText(Component.nullToEmpty("Missing translation: " + key))));
		//$$ }
	//$$ }
	//#else
//	public Component translate(final String key, Object... args)
//	{
//		if (this.hasTranslation(key))
//		{
//			return new TextComponent()
//		}
//		else
//		{
//			return new TextComponent(key);
//		}
//	}
	//#endif
}
