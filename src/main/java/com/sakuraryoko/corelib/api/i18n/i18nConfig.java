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

package com.sakuraryoko.corelib.api.i18n;

import com.google.common.collect.ImmutableList;
import com.sakuraryoko.corelib.impl.i18n.i18nLang;
import com.sakuraryoko.corelib.impl.i18n.i18nManager;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nonnull;

public class i18nConfig implements StringRepresentable
{
	private final ImmutableList<i18nOption> options;
	private final i18nManager manager;
	private i18nOption selectedOption;
	private int selectedIndex;

	public i18nConfig(@Nonnull i18nManager manager)
	{
		this.manager = manager;
		this.options = ImmutableList.copyOf(manager.getLanguageOptions());
		this.selectedOption = manager.getLang().toOption();
		this.calculateIndex();
	}

	private void calculateIndex()
	{
		for (int i = 0; i < this.options.size(); i++)
		{
			i18nOption option = this.options.get(i);

			if (option == this.selectedOption)
			{
				this.selectedIndex = i;
				break;
			}
		}
	}

	public boolean isModified()
	{
		return !this.getManager().getDefaultLang().getLangCode().equalsIgnoreCase(this.getManager().getLang().getLangCode());
	}

	public void resetToDefault()
	{
		this.manager.resetLangToDefault();
		this.selectedOption = manager.getLang().toOption();
		this.calculateIndex();
	}

	public i18nManager getManager()
	{
		return this.manager;
	}

	public i18nOption getSelectedOption()
	{
		return this.selectedOption;
	}

	public i18nLang getDefaultLang()
	{
		return this.manager.getDefaultLang();
	}

	public i18nLang getLang()
	{
		return this.manager.getLang();
	}

	public String getStringValue()
	{
		return this.selectedOption.getKey();
	}

	public String getDisplayName()
	{
		return this.selectedOption.getTranslatedName();
	}

	@Override
	public @NonNull String getSerializedName()
	{
		return this.getStringValue();
	}

	public i18nConfig cycle(boolean forward)
	{
		int id = this.selectedIndex;
		int length = this.options.size();

		if (forward)
		{
			if (++id >= length)
			{
				id = 0;
			}
		}
		else
		{
			if (--id < 0)
			{
				id = length - 1;
			}
		}

		this.selectedIndex = id % length;
		this.selectedOption = this.options.get(this.selectedIndex);
		this.manager.setLang(this);

		return this;
	}

	public i18nConfig fromString(String value)
	{
		this.selectedOption = i18nOption.fromString(value);
		this.manager.setLang(this);
		return this;
	}
}
