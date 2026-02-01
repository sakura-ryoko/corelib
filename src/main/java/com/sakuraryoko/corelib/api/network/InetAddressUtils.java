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

package com.sakuraryoko.corelib.api.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import javax.annotation.Nullable;

import com.sakuraryoko.corelib.impl.CoreLib;

public class InetAddressUtils
{
	/**
	 * Resolve a String representation of a Host Address
	 * using your system's default DNS Resolver.
	 * @param address   Address in String form.  Can be either an IP Address, or a hostname, or {@link null}.
	 * @return          {@link Optional} of {@link InetAddress}
	 */
	public static Optional<InetAddress> resolveAddress(@Nullable String address)
	{
		try
		{
			return Optional.of(InetAddress.getByName(address));
		}
		catch (UnknownHostException e)
		{
			CoreLib.LOGGER.error("resolveAddress: Exception: {}", e.getLocalizedMessage());
		}

		return Optional.empty();
	}

	/**
	 * Return the 'Host Address' portion of an {@link InetAddress}
	 * @param addr      The {@link InetAddress} to use
	 * @return          {@link Optional} of the Host Address
	 */
	public static Optional<String> getHostAddress(@Nullable InetAddress addr)
	{
		if (addr == null)
		{
			return Optional.empty();
		}

		return Optional.of(addr.getHostAddress());
	}

	/**
	 * Return the Loopback Address using {@link InetAddress}
	 * @return      The Loopback Address
	 */
	public static InetAddress getLoopbackAddress()
	{
		return InetAddress.getLoopbackAddress();
	}

	/**
	 * Resolve the local machine's default Host Address.
	 * @return      {@link Optional} of the Host Address as an {@link InetAddress}
	 */
	public static Optional<InetAddress> resolveLocalHostAddress()
	{
		try
		{
			return Optional.of(InetAddress.getLocalHost());
		}
		catch (UnknownHostException e)
		{
			CoreLib.LOGGER.error("resolveLocalHostAddress: Exception: {}", e.getLocalizedMessage());
		}

		return Optional.empty();
	}
}
