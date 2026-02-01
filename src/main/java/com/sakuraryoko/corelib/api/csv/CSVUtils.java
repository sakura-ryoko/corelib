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

package com.sakuraryoko.corelib.api.csv;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;

import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.impl.csv.CSVFileCache;
import com.sakuraryoko.corelib.impl.csv.CSVHeader;
import com.sakuraryoko.corelib.impl.csv.CSVWrapper;

/**
 * CSV File Reading / Writing Utilities.<br>
 * This interface was cloned from my CSVTool program available on GitHub;
 * which utilizies the OpenCSV library to do Advanced CSV file manipulation.
 */
public class CSVUtils
{
	private static final AnsiLogger LOGGER = new AnsiLogger(CSVUtils.class);

	/**
	 * Read a CSV file into a {@link CSVFileCache}.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	public static CSVFileCache readFile(@Nullable String file)
	{
		return readFile(file, true, true, false, false);
	}

	/**
	 * Read a CSV file into a {@link CSVFileCache}.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @param ignoreQuotes  Enable Ignore Quotes in parsing the CSV files
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	public static CSVFileCache readFile(@Nullable String file, boolean ignoreQuotes)
	{
		return readFile(file, true, ignoreQuotes, false, false);
	}

	/**
	 * Read a CSV file into a {@link CSVFileCache}.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @param readHeader    Read the CSV Header
	 * @param ignoreQuotes  Enable Ignore Quotes in parsing the CSV files
	 * @param debug         Enable Debug logger
	 * @param ansiColor     Enable Ansi Color logger
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	@VisibleForTesting
	public static CSVFileCache readFile(@Nullable String file, boolean readHeader, boolean ignoreQuotes,
	                                    boolean debug, boolean ansiColor)
	{
		if (file == null || file.isEmpty())
		{
			LOGGER.error("readFile(): file is empty");
			return null;
		}

		LOGGER.toggleDebug(debug);
		LOGGER.toggleAnsiColor(ansiColor);
		LOGGER.debug("readFile(): Reading file [{}] ...", file);

		try (CSVWrapper wrapper = new CSVWrapper(file))
		{
			wrapper.setDebug(debug, ansiColor);

			if (wrapper.read(readHeader, ignoreQuotes))
			{
				LOGGER.debug("readFile(): File read");
				CSVFileCache cache = new CSVFileCache();

				cache.setDebug(debug, ansiColor);
				cache.copyFile(wrapper);
				LOGGER.toggleDebug(false);
				LOGGER.toggleAnsiColor(false);

				return cache;
			}
		}
		catch (Exception e)
		{
			LOGGER.error("readFile(): Exception reading file; {}", e.getLocalizedMessage());
		}

		return null;
	}

	/**
	 * Read a CSV file's into a {@link CSVFileCache} -- headers only, with no file contents.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	public static CSVFileCache readFileHeadersOnly(@Nullable String file)
	{
		return readFileHeadersOnly(file, true, false, false);
	}

	/**
	 * Read a CSV file's into a {@link CSVFileCache} -- headers only, with no file contents.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @param ignoreQuotes  Enable Ignore Quotes in parsing the CSV files
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	public static CSVFileCache readFileHeadersOnly(@Nullable String file, boolean ignoreQuotes)
	{
		return readFileHeadersOnly(file, ignoreQuotes, false, false);
	}

	/**
	 * Read a CSV file's into a {@link CSVFileCache} -- headers only, with no file contents.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param file          The file to read
	 * @param ignoreQuotes  Enable Ignore Quotes in parsing the CSV files
	 * @param debug         Enable Debug logger
	 * @param ansiColor     Enable Ansi Color logger
	 * @return              The CSV File Cache or null.
	 */
	@Nullable
	@VisibleForTesting
	public static CSVFileCache readFileHeadersOnly(@Nullable String file, boolean ignoreQuotes,
	                                               boolean debug, boolean ansiColor)
	{
		if (file == null || file.isEmpty())
		{
			LOGGER.error("readFileHeadersOnly(): file is empty");
			return null;
		}

		LOGGER.toggleDebug(debug);
		LOGGER.toggleAnsiColor(ansiColor);
		LOGGER.debug("readFileHeadersOnly(): Reading file [{}] ...", file);

		try (CSVWrapper wrapper = new CSVWrapper(file))
		{
			wrapper.setDebug(debug, ansiColor);

			if (wrapper.readHeadersOnly(ignoreQuotes))
			{
				LOGGER.debug("readFileHeadersOnly(): File read!");
				CSVFileCache cache = new CSVFileCache();

				cache.setDebug(debug, ansiColor);
				cache.copyFileHeadersOnly(wrapper);
				LOGGER.toggleDebug(false);
				LOGGER.toggleAnsiColor(false);

				return cache;
			}
		}
		catch (Exception e)
		{
			LOGGER.error("readFileHeadersOnly(): Exception reading file: {}", e.getLocalizedMessage());
		}

		return null;
	}

	/**
	 * Compare two {@link CSVHeader} to check to see that they match, and it also can match empty.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param header1       The First header
	 * @param header2       The Second header
	 * @return              Whether the headers are equal, or if one is null
	 */
	public static boolean compareHeaders(@Nullable CSVHeader header1, @Nullable CSVHeader header2)
	{
		return compareHeaders(header1, header2, false, false);
	}

	/**
	 * Compare two {@link CSVHeader} to check to see that they match, and it also can match empty.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param header1       The First header
	 * @param header2       The Second header
	 * @param debug         Enable Debug logger
	 * @param ansiColor     Enable Ansi Color logger
	 * @return              Whether the headers are equal, or if one is null
	 */
	@VisibleForTesting
	public static boolean compareHeaders(@Nullable CSVHeader header1, @Nullable CSVHeader header2,
	                                     boolean debug, boolean ansiColor)
	{
		if (header1 == null || header2 == null)
		{
			return false;
		}

		LOGGER.toggleDebug(debug);
		LOGGER.toggleAnsiColor(ansiColor);
		header1.setDebug(debug, ansiColor);
		header2.setDebug(debug, ansiColor);
		LOGGER.debug("compareHeaders(): ...");

		boolean result = header1.matches(header2);

		LOGGER.toggleDebug(false);
		LOGGER.toggleAnsiColor(false);

		return result;
	}

	/**
	 * Write a {@link CSVFileCache} to disk, with an optional append mode.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 * *
	 * @param file          The {@link CSVFileCache} to write
	 * @return              Whether the file was written to disk
	 */
	public static boolean writeFile(@Nullable CSVFileCache file)
	{
		return writeFile(file, false, false, null, false, false);
	}

	/**
	 * Write a {@link CSVFileCache} to disk, with an optional append mode.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 * *
	 * @param file          The {@link CSVFileCache} to write
	 * @param applyQuotes   Enable forcing the CSV file to have all values surrounded by Quotes
	 * @return              Whether the file was written to disk
	 */
	public static boolean writeFile(@Nullable CSVFileCache file, boolean applyQuotes)
	{
		return writeFile(file, applyQuotes, false, null, false, false);
	}

	/**
	 * Write a {@link CSVFileCache} to disk, with an optional append mode.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 * *
	 * @param file          The {@link CSVFileCache} to write
	 * @param applyQuotes   Enable forcing the CSV file to have all values surrounded by Quotes
	 * @param appendMode    Enable Append mode, which requires a {@link CSVFileCache} append
	 * @param append        The {@link CSVFileCache} to append
	 * @return              Whether the file was written to disk
	 */
	public static boolean writeFile(@Nullable CSVFileCache file, boolean applyQuotes,
	                                boolean appendMode, @Nullable CSVFileCache append)
	{
		return writeFile(file, applyQuotes, appendMode, append, false, false);
	}

	/**
	 * Write a {@link CSVFileCache} to disk, with an optional append mode.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 * *
	 * @param file          The {@link CSVFileCache} to write
	 * @param applyQuotes   Enable forcing the CSV file to have all values surrounded by Quotes
	 * @param appendMode    Enable Append mode, which requires a {@link CSVFileCache} append
	 * @param append        The {@link CSVFileCache} to append
	 * @param debug         Enable Debug logger
	 * @param ansiColor     Enable Ansi Color logger
	 * @return              Whether the file was written to disk
	 */
	@VisibleForTesting
	public static boolean writeFile(@Nullable CSVFileCache file, boolean applyQuotes,
	                                boolean appendMode, @Nullable CSVFileCache append,
	                                boolean debug, boolean ansiColor)
	{
		if (file == null || file.isEmpty())
		{
			LOGGER.error("writeFile(): file is empty");
			return false;
		}

		LOGGER.toggleDebug(debug);
		LOGGER.toggleAnsiColor(ansiColor);
		file.setDebug(debug, ansiColor);
		if (append != null)
		{
			append.setDebug(debug, ansiColor);
		}

		LOGGER.debug("writeFile(): Write file [{}]:", file.getFileName());

		try (CSVWrapper wrapper = new CSVWrapper(file.getFileName(), false))
		{
			wrapper.setDebug(debug, ansiColor);

			if (wrapper.putAllLines(file.getFile(), true))
			{
				if (append != null)
				{
					if (!appendFile(wrapper, append, debug, ansiColor))
					{
						LOGGER.error("writeFile(): Error appending file Cache");
						wrapper.close();
						LOGGER.toggleDebug(false);
						LOGGER.toggleAnsiColor(false);

						return false;
					}
				}

				if (wrapper.write(applyQuotes, appendMode))
				{
					LOGGER.debug("writeFile(): File written");
					wrapper.close();
					LOGGER.toggleDebug(false);
					LOGGER.toggleAnsiColor(false);

					return true;
				}
			}
			else
			{
				LOGGER.error("writeFile(): Error copying file Cache to new file");
				wrapper.close();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("writeFile(): Exception writing file: {}", e.getLocalizedMessage());
		}

		LOGGER.toggleDebug(false);
		LOGGER.toggleAnsiColor(false);
		return false;
	}

	public static boolean appendFile(@Nullable CSVWrapper wrapper, @Nullable CSVFileCache file)
	{
		return appendFile(wrapper, file, false, false);
	}

	/**
	 * Append a {@link CSVWrapper} using a {@link CSVFileCache} as the input.<br>
	 * Function params are only marked as @Nullable to communicate that the function is Null safe.
	 *
	 * @param wrapper       The {@link CSVWrapper} to append to.
	 * @param file          The {@link CSVFileCache} to append with
	 * @param debug         Enable Debug logger
	 * @param ansiColor     Enable Ansi Color logger
	 * @return              Whether the {@link CSVWrapper} was appended to
	 */
	@VisibleForTesting
	public static boolean appendFile(@Nullable CSVWrapper wrapper, @Nullable CSVFileCache file,
	                                 boolean debug, boolean ansiColor)
	{
		if (wrapper == null || wrapper.isEmpty())
		{
			LOGGER.error("appendFile(): CSVWrapper is empty");
			return false;
		}
		else if (file == null || file.isEmpty())
		{
			LOGGER.error("appendFile(): FILE is empty");
			return false;
		}

		LOGGER.toggleDebug(debug);
		LOGGER.toggleAnsiColor(ansiColor);
		wrapper.setDebug(debug, ansiColor);
		file.setDebug(debug, ansiColor);
		LOGGER.debug("appendFile(): Appending file to wrapper...");

		for (int i = 1; i < file.getFile().size(); i++)
		{
			List<String> entry = file.getFile().get(i);

			if (!entry.isEmpty())
			{
				wrapper.putLine(entry);
			}
		}

		LOGGER.toggleDebug(false);
		LOGGER.toggleAnsiColor(false);

		return true;
	}
}
