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

package com.sakuraryoko.corelib.impl.core.config.data.opt;

import com.sakuraryoko.corelib.api.config.IConfigOption;
import com.sakuraryoko.corelib.api.util.MathUtils;

public class CoreConfigOptions implements IConfigOption
{
	public boolean enableNetworkService;
	public int networkServicePort;

	public CoreConfigOptions()
	{
		this.enableNetworkService = false;
		this.networkServicePort = -1;
	}

	@Override
	public void defaults()
	{
		this.enableNetworkService = false;
		this.networkServicePort = -1;
	}

	@Override
	public CoreConfigOptions copy(IConfigOption opt)
	{
		if (opt instanceof CoreConfigOptions)
		{
			CoreConfigOptions o = (CoreConfigOptions) opt;

			this.enableNetworkService = o.enableNetworkService;
			this.networkServicePort = MathUtils.clamp(o.networkServicePort, -1, 65535);
		}

		return this;
	}
}
