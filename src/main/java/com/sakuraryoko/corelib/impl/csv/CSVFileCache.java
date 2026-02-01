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

package com.sakuraryoko.corelib.impl.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;

import com.sakuraryoko.corelib.api.log.AnsiLogger;

public class CSVFileCache implements AutoCloseable
{
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass());

    public HashMap<Integer, List<String>> file;
    public CSVHeader header;
    public String fileName;

    public CSVFileCache()
    {
        this.file = new HashMap<>();
        this.header = new CSVHeader();
        this.fileName = "";
    }

    public CSVFileCache(@Nonnull CSVHeader newHeader)
    {
        this.file = new HashMap<>();
        this.setHeader(newHeader);
        this.fileName = "";
    }

    public CSVFileCache(@Nonnull CSVHeader newHeader, String fileName)
    {
        this.file = new HashMap<>();
        this.setHeader(newHeader);
        this.fileName = fileName;
    }

    public void setDebug(boolean debug, boolean ansiColor)
    {
        LOGGER.toggleDebug(debug);
        LOGGER.toggleAnsiColor(ansiColor);
    }

    public void copyFile(CSVWrapper wrapper)
    {
        if (!this.file.isEmpty())
        {
            this.file.clear();
        }

        LOGGER.debug("copyFile(): Caching file [{} lines] ...", wrapper.getSize());
        this.copyHeader(wrapper);
        this.setFileName(wrapper.getFile());
        this.file.putAll(wrapper.getAllLines());
    }

    public void copyFileHeadersOnly(CSVWrapper wrapper)
    {
        if (!this.file.isEmpty())
        {
            this.file.clear();
        }

        LOGGER.debug("copyFileHeadersOnly(): Copying file Headers ...");
        this.copyHeader(wrapper);
        this.setFileName(wrapper.getFile());
    }

    public void copyHeader(CSVWrapper wrapper)
    {
        LOGGER.debug("copyHeader(): Caching Header ...", wrapper.getSize());
        this.header = wrapper.getHeader();
    }

    public HashMap<Integer, List<String>> getFile()
    {
        return this.file;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    public void setHeader(CSVHeader header)
    {
        LOGGER.debug("setHeader(): Setting Header");
        this.header = header;

        // Add header to FILE
        this.file.put(0, header.stream().collect(Collectors.toList()));
    }

    public void appendHeader(String header)
    {
        LOGGER.debug("appendHeader(): Appending Header");
        this.header.add(header);

        // Replace headers to FILE
        List<String> newHeaders = this.header.stream().collect(Collectors.toList());
        this.file.put(0, newHeaders);
    }

    public CSVFileCache setFileName(String name)
    {
        this.fileName = name;
        return this;
    }

    public boolean hasLine(int line)
    {
        return this.file.containsKey(line);
    }

    public List<String> getLine(int line)
    {
        if (line > this.file.size())
        {
            return Lists.newArrayList();
        }

        return this.file.get(line);
    }

    public void addLine(List<String> line)
    {
        List<String> out = new ArrayList<>();

        // Ensure Line Size fits
        if (line.size() < this.header.size())
        {
            for (int j = line.size(); j < this.header.size(); j++)
            {
                line.add("");
            }
        }

        for (int i = 0; i < this.header.size(); i++)
        {
            out.add(line.get(i));
        }

//        LOGGER.debug("addLine({}): out [{}]", this.file.size(), out.toString());
        this.file.put(this.file.size(), out);
    }

    public void setLine(int line, List<String> data)
    {
        List<String> out = new ArrayList<>();

        // Ensure Data Size fits
        if (data.size() < this.header.size())
        {
            for (int j = data.size(); j < this.header.size(); j++)
            {
                data.add("");
            }
        }

        for (int i = 0; i < this.header.size(); i++)
        {
            out.add(data.get(i));
        }

//        LOGGER.debug("setLine({}): out [{}]", this.file.size(), out.toString());
        this.file.put(line, out);
    }

    public CSVHeader getHeader()
    {
        return this.header;
    }

    public boolean isEmpty()
    {
        return this.file.isEmpty() || this.file.size() == 1;
    }

    public void clear()
    {
        if (this.file != null && !this.file.isEmpty())
        {
            this.file.clear();
        }

        if (this.header != null && !this.header.isEmpty())
        {
            this.header.clear();
        }
    }

    @Override
    public void close() throws Exception
    {
        if (this.file != null)
        {
            this.file.clear();
        }
        if (this.header != null)
        {
            this.header.close();
        }
    }
}
