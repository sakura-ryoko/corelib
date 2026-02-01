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

package com.sakuraryoko.corelib.api.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sakuraryoko.corelib.api.log.AnsiLogger;

public class FileUtils
{
    private static final AnsiLogger LOGGER = new AnsiLogger(FileUtils.class);
    private static final String REGEX_SANITIZE = "[\\\\/:*?\"<>|]|\\p{C}|\\p{M}";

    public static boolean fileExists(String path)
    {
        try
        {
            Path test = Paths.get(path);
            return Files.exists(test);
        }
        catch (Exception err)
        {
            LOGGER.error("fileExists(): Exception checking if file [\"{}\"] exists. [{}]", path, err.getMessage());
        }

        return false;
    }

    public static boolean deleteIfExists(String path)
    {
        try
        {
            Path file = Paths.get(path);

            if (Files.exists(file))
            {
                LOGGER.debug("deleteIfExists(): Deleting file [{}] ...", path);
                Files.delete(file);
            }

            return true;
        }
        catch (Exception err)
        {
            LOGGER.error("deleteIfExists(): Exception deleting file [\"{}\"]. [{}]", path, err.getMessage());
        }

        return false;
    }

    public static boolean checkIfDirectoryExists(Path dir)
    {
        try
        {
            return Files.exists(dir) && Files.isDirectory(dir) && Files.isReadable(dir);
        }
        catch (Exception err)
        {
            LOGGER.error("checkIfDirectoryExists(): Exception checking for directory '{}'; {}", dir.toAbsolutePath().toString(), err.getLocalizedMessage());
        }

        return false;
    }

    public static String sanitizeFileName(String fileIn)
    {
        return fileIn.replaceAll(REGEX_SANITIZE, "");
    }
}
