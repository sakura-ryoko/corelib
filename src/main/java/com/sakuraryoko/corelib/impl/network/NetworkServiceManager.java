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

package com.sakuraryoko.corelib.impl.network;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sakuraryoko.corelib.api.network.InetAddressUtils;
import com.sakuraryoko.corelib.api.util.MathUtils;
import com.sakuraryoko.corelib.impl.Reference;
import com.sakuraryoko.corelib.impl.core.modinit.CoreInit;

public class NetworkServiceManager
{
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID+":network_service_manager");
	private static final NetworkServiceManager INSTANCE = new NetworkServiceManager();
	public static NetworkServiceManager getInstance() { return INSTANCE; }

	private boolean isServerStarted;
	private boolean isClientStarted;
	private InetAddress localAddress;
	private int localPort;

	private NetworkServiceManager()
	{
		this.isServerStarted = false;
		this.isClientStarted = false;
		this.resolveHostAndPort();
	}

	private void resolveHostAndPort()
	{
		this.localAddress = InetAddressUtils.resolveLocalHostAddress().orElse(InetAddress.getLoopbackAddress());
		this.localPort = MathUtils.clamp(ThreadLocalRandom.current().nextInt(666, 65535), 666, 65535);
	}

	public InetAddress getLocalAddress()
	{
		return this.localAddress;
	}

	public int getLocalPort()
	{
		return this.localPort;
	}

	public boolean isServer()
	{
		return  CoreInit.getInstance().isServer() ||
				CoreInit.getInstance().isDedicatedServer() ||
				CoreInit.getInstance().isIntegratedServer() ||
				CoreInit.getInstance().isOpenToLan();
	}

	public boolean isClient()
	{
		return CoreInit.getInstance().isClient();
	}

	public boolean isServerStarted()
	{
		return this.isServerStarted;
	}

	public boolean isClientStarted()
	{
		return this.isClientStarted;
	}

	public void onStartServer()
	{
		if (this.isServer())
		{
			LOGGER.info("Server has been started");
			this.isServerStarted = true;
			return;
		}
	}

	public void onStopServer()
	{
		if (this.isServer())
		{
			LOGGER.info("Server has been stopped");
			this.isServerStarted = false;
			return;
		}
	}

	public void onStartClient()
	{
		if (this.isClient())
		{
			LOGGER.info("Client has been started");
			this.isClientStarted = true;
			return;
		}
	}

	public void onStopClient()
	{
		if (this.isClient())
		{
			LOGGER.info("Client has been stopped");
			this.isClientStarted = false;
			return;
		}
	}

	public void onReset()
	{
		if (this.isClientStarted())
		{
			this.onStopClient();
		}

		if (this.isServerStarted())
		{
			this.onStopServer();
		}

		this.reset();
	}

	public void reset()
	{
		this.isServerStarted = false;
		this.isClientStarted = false;
	}
}
