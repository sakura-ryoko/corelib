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

import com.sakuraryoko.corelib.api.i18n.i18nConfig;
import com.sakuraryoko.corelib.api.i18n.i18nOption;
import com.sakuraryoko.corelib.impl.CoreLib;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.text.BuiltinTextHandler;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class i18nManager
{
	public static final i18nFileFilter FILE_FILTER = new i18nFileFilter();
	public static final String DEFAULT_LANG = "en_us";
	private final List<String> keys;
	private final List<i18nOption> options;
	private final i18nLang defaultLang;
	private final String modId;
	private final String baseString;
	private i18nLang lang;

	private i18nManager(String modId) throws IOException
	{
		this.modId = modId;
		this.keys = new ArrayList<>();
		this.options = new ArrayList<>();
		this.baseString = "assets/"+this.modId+"/lang";
		this.defaultLang = i18nLang.load(this.baseString, DEFAULT_LANG);
		this.lang = null;
		this.readKeys();
	}

	@Nullable
	public static i18nManager create(String modId)
	{
		try
		{
			i18nManager result = new i18nManager(modId);

			if (result.defaultLang == null)
			{
				CoreLib.LOGGER.error("i18nOptionManager#create({}): Default language: '{}' not found!", modId, DEFAULT_LANG);
			}

			return result;
		}
		catch (IOException e)
		{
			CoreLib.LOGGER.error("i18nOptionManager#create({}): Exception building i18nManager; {}", modId, e.getLocalizedMessage());
		}

		return null;
	}

	@ApiStatus.Internal
	private void readKeys()
	{
		final String baseString = "/"+this.baseString;
		URL resource = i18nManager.class.getResource(baseString);

		if (resource != null)
		{
			try
			{
				URI uri = resource.toURI();
				Path root;
				FileSystem tempFs = null;
				boolean created = false;

				if (uri.getScheme().equals("jar"))
				{
					try
					{
						tempFs = FileSystems.getFileSystem(uri);
					}
					catch (FileSystemNotFoundException e)
					{
						tempFs = FileSystems.newFileSystem(uri, Collections.emptyMap());
						created = true;
					}

					root = tempFs.getPath(baseString);
				}
				else
				{
					root = Paths.get(uri);
				}

				this.keys.clear();

				try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, FILE_FILTER))
				{
					for (Path file : stream)
					{
						final String fileName = file.getFileName().toString();
						final String nameOnly = fileName.split("\\.")[0];
						this.keys.add(nameOnly);
					}
				}
				finally
				{
					if (tempFs != null && tempFs.isOpen()
						&& created)
					{
						tempFs.close();
					}
				}
			}
			catch (Exception e)
			{
				CoreLib.LOGGER.error("i18nOptionManager#readKeys({}): Could not find resource '{}'; Exception: {}", this.getModId(), this.baseString, e.getLocalizedMessage());
			}
		}

		if (this.keys.isEmpty())
		{
			this.keys.add(DEFAULT_LANG);
		}

		this.buildLanguageOptions();

		if (Reference.DEBUG)
		{
			CoreLib.LOGGER.info("i18nOptionManager#readKeys({}): keys read from assets folder: {}", this.getModId(), this.keys.toString());
		}
	}

	@ApiStatus.Internal
	private void buildLanguageOptions()
	{
		this.options.clear();

		for (String key : this.keys)
		{
			i18nOption opt = i18nOption.fromString(key);

			if (opt != null)
			{
				this.options.add(opt);
			}
			else
			{
				CoreLib.LOGGER.warn("i18nOptionManager#buildLanguageOptions({}): Language file: '{}' not matched", this.getModId(), key);
			}
		}

		CoreLib.LOGGER.warn("i18nOptionManager#buildLanguageOptions({}): Detected [{}] available options.", this.getModId(), this.options.size());
	}

	public String getModId()
	{
		return this.modId;
	}

	public String getBaseString()
	{
		return this.baseString;
	}

	public i18nLang getDefaultLang()
	{
		return this.defaultLang;
	}

	public i18nLang getLang()
	{
		this.ensureLang();
		return this.lang;
	}

	public void resetLangToDefault()
	{
		this.lang = this.defaultLang;
	}

	public void setLang(i18nConfig config)
	{
		if (this.lang != null && Objects.equals(this.lang.getLangCode(), config.getStringValue()))
		{
			// Already matches
			return;
		}

		// Change
		this.lang = null;

		try
		{
			this.lang = i18nLang.load(this.baseString, config.getStringValue());
			CoreLib.LOGGER.info("i18nOptionManager#setLang({}): Language: '{}' [{}] - has been loaded successfully.", this.getModId(), config.getStringValue(), config.getDisplayName());
		}
		catch (IOException e)
		{
			this.ensureLang();
			CoreLib.LOGGER.error("i18nOptionManager#setLang({}): Exception loading language: '{}'; {}", this.getModId(), config.getStringValue(), e.getLocalizedMessage());
		}
	}

	public List<i18nOption> getLanguageOptions()
	{
		this.ensureLang();
		return this.options;
	}

	@ApiStatus.Internal
	private void ensureLang()
	{
		if (this.lang == null)
		{
			this.lang = this.defaultLang;
		}
	}

	public boolean hasTranslation(String key)
	{
		this.ensureLang();
		return this.lang.hasTranslation(key);
	}

	public String translateOrFallback(String key, String fallback)
	{
		this.ensureLang();

		if (this.lang.hasTranslation(key))
		{
			return this.translate(key);
		}

		return fallback;
	}

	public String translate(String key, Object... args)
	{
		this.ensureLang();
		String result = this.lang.getOrDefault(key, key);

		try
		{
			return String.format(Locale.ROOT, result, args);
		}
		catch (Exception e)
		{
			CoreLib.debugLog("i18nOptionManager#translate({}): Formatting exception for key: {}; {}", this.getModId(), key, e.getLocalizedMessage());
			return "Format Error: "+result;
		}
	}

	public Component translateAsText(String key, Object... args)
	{
		this.ensureLang();
		return BuiltinTextHandler.getInstance().of(this.translate(key, args));
	}

	// matches 'en_us.json'; for example.
	@ApiStatus.Internal
	public static class i18nFileFilter implements DirectoryStream.Filter<Path>
	{
		private final Pattern pattern1 = Pattern.compile("^[a-z]{2,4}_[a-z]{2,4}$");
		private final Pattern pattern2 = Pattern.compile("^[a-z]{3,4}$");

		@Override
		public boolean accept(Path entry) throws IOException
		{
			try
			{
				final String fullName = entry.getFileName().toString();

				if (Files.isRegularFile(entry) && fullName.endsWith(".json"))
				{
					final String nameOnly = fullName.split("\\.")[0];

					return  this.pattern1.matcher(nameOnly).matches() ||
							this.pattern2.matcher(nameOnly).matches();
				}

				return false;
			}
			catch (Exception err)
			{
				throw new IOException(err.getLocalizedMessage());
			}
		}
	}
}
