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

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.opencsv.*;

import com.sakuraryoko.corelib.api.log.AnsiLogger;
import com.sakuraryoko.corelib.api.util.FileUtils;

@Immutable
public class CSVWrapper implements AutoCloseable
{
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass());
    private CSVParser parser;
    private CSVReader reader;
    private CSVWriter writer;

    private final String file;
    private HashMap<Integer, String> header;
    private HashMap<Integer, List<String>> lines;
    private int columns;
    private final boolean read;

    public CSVWrapper(String file)
    {
        this(file, true);
    }

    public CSVWrapper(String file, boolean read)
    {
        this.file = file;
        this.parser = null;
        this.reader = null;
        this.writer = null;
        this.header = new HashMap<>();
        this.lines = new HashMap<>();
        this.columns = -1;
        this.read = read;
    }

    public void setDebug(boolean debug, boolean ansiColor)
    {
        LOGGER.toggleDebug(debug);
        LOGGER.toggleAnsiColor(ansiColor);
    }

    private CSVParser getParser(boolean ignoreQuotes)
    {
        if (this.parser == null)
        {
            LOGGER.debug("getParser(): Building Parser ...");
            this.parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .withIgnoreQuotations(ignoreQuotes)
                    //.withStrictQuotes(!ignoreQuotes)
                    .build();
        }

        return this.parser;
    }

    private @Nullable CSVReader getReader(boolean ignoreQuotes)
    {
        try
        {
            if (this.reader != null)
            {
                this.reader.close();
            }

            LOGGER.debug("getReader(): Building Reader ...");
            this.reader = new CSVReaderBuilder(new FileReader(this.file))
                    .withCSVParser(this.getParser(ignoreQuotes))
                    .build();
        }
        catch (Exception e)
        {
            LOGGER.error("getReader(): Exception reading input file [{}], error: [{}]", this.file, e.getMessage());
            return null;
        }

        return this.reader;
    }

    public boolean read()
    {
        return this.read(true, true);
    }

    public boolean read(boolean withHeader, boolean ignoreQuotes)
    {
        if (!this.read)
        {
            LOGGER.warn("read(): for file [{}] is not a reader", this.file);
            return false;
        }

        if (this.getReader(ignoreQuotes) == null)
        {
            LOGGER.error("read(): for file [{}] failed to build a CSVReader", this.file);
            return false;
        }

        this.lines = new HashMap<>();
        AtomicInteger line = new AtomicInteger();

        LOGGER.debug("read(): Reading file ...");
        this.reader.forEach((str) ->
        {
            // Read header
            if (withHeader && line.get() == 0)
            {
                LOGGER.debug("read(): Reading headers... ");

                for (int i = 0; i < str.length; i++)
                {
                    this.header.put(i, str[i]);
                }

                this.columns = this.header.size();
            }

            List<String> entry = this.hasHeader() ? this.truncateLine(new ArrayList<>(Arrays.asList(str))) : new ArrayList<>(Arrays.asList(str));
            this.lines.put((line.getAndIncrement()), entry);
        });

        try
        {
            LOGGER.debug("read(): Lines read [{}]", this.getSize());
            this.reader.close();
            this.reader = null;
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error("read(): Exception reading file [{}]", this.file);
        }

        return false;
    }

    public boolean readHeadersOnly(boolean ignoreQuotes)
    {
        if (!this.read)
        {
            LOGGER.warn("readHeadersOnly(): for file [{}] is not a reader", this.file);
            return false;
        }

        if (this.getReader(ignoreQuotes) == null)
        {
            LOGGER.error("readHeadersOnly(): for file [{}] failed to build a CSVReader", this.file);
            return false;
        }

        this.lines = new HashMap<>();
        LOGGER.debug("readHeadersOnly(): Reading file ...");

        try
        {
            LOGGER.debug("readHeadersOnly(): Reading headers... ");
            String[] str = this.reader.readNext();

            for (int i = 0; i < str.length; i++)
            {
                this.header.put(i, str[i]);
            }

            this.columns = this.header.size();
        }
        catch (Exception err)
        {
            LOGGER.error("readHeadersOnly(): Exception reading file [{}]", this.file);
        }

        try
        {
            LOGGER.debug("readHeadersOnly(): Lines read [{}]", this.getSize());
            this.reader.close();
            this.reader = null;
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error("readHeadersOnly(): Exception reading file [{}]", this.file);
        }

        return false;
    }

    private @Nullable CSVWriter getWriter()
    {
        return this.getWriter(false);
    }

    private @Nullable CSVWriter getWriter(boolean append)
    {
        try
        {
            if (this.writer != null)
            {
                this.writer.close();
            }

            if (FileUtils.deleteIfExists(this.file))
            {
                LOGGER.debug("getWriter(): File [{}] deleted or not exists.", this.file);
            }
            else
            {
                LOGGER.error("getWriter(): Exception deleting file [{}]", this.file);
                return null;
            }

            LOGGER.debug("getWriter(): Building Writer ...");
            this.writer = new CSVWriter(new FileWriter(this.file, append));
        }
        catch (Exception e)
        {
            LOGGER.error("getWriter(): Exception opening output file [{}], error: [{}]", this.file, e.getMessage());
            return null;
        }

        return this.writer;
    }

    public boolean write()
    {
        return this.write(false, false);
    }

    public boolean write(boolean applyQuotes, boolean append)
    {
        if (this.read)
        {
            LOGGER.warn("write(): for file [{}] is not a writer!", this.file);
            return false;
        }

        if (this.isEmpty())
        {
            LOGGER.error("write(): for file [{}] is Empty!", this.file);
            return false;
        }

        if (append)
        {
            // Append mode needs matching headers
            try (CSVWrapper wrapper = new CSVWrapper(this.file, true))
            {
                if (wrapper.read(true, false))
                {
                    CSVHeader csvHeader = wrapper.getHeader();
                    CSVHeader newHeader = this.getHeader();

                    if (csvHeader == null || newHeader == null)
                    {
                        LOGGER.error("write(): Append headers check failed (One or the other heads are missing!)");
                        return false;
                    }

                    if (!csvHeader.matches(newHeader))
                    {
                        LOGGER.error("write(): Append headers check failed (Not matched!)");
                        return false;
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.error("write(): Exception opening output file [{}] for append check, error: [{}]", this.file, e.getMessage());
                return false;
            }
        }

        if (this.getWriter(append) == null)
        {
            LOGGER.error("write(): for file [{}] failed to build a CSVWriter!", this.file);
            return false;
        }

        for (int i = 0; i < this.lines.size(); i++)
        {
            String[] entry = this.lines.get(i).toArray(new String[0]);
            this.writer.writeNext(entry, applyQuotes);
        }

        try
        {
            this.writer.close();
            this.writer = null;
            return true;
        }
        catch (Exception e)
        {
            LOGGER.error("write(): Exception writing file [{}]", this.file);
        }

        return false;
    }

    public String getFile()
    {
        return this.file;
    }

    public boolean hasHeader()
    {
        return this.header != null && !this.header.isEmpty();
    }

    public @Nullable CSVHeader getHeader()
    {
        if (this.header.isEmpty())
        {
            LOGGER.error("getHeader(): Header is empty!");
            return null;
        }

        LOGGER.debug("getHeader(): Building new Header ...");
        CSVHeader csvHeader = new CSVHeader();
        List<String> list = new ArrayList<>();

        this.header.forEach((h, s) -> list.add(s));
        csvHeader.setHeaders(list);

        return csvHeader;
    }

    public @Nullable CSVHeader setHeader(@Nonnull List<String> list)
    {
        if (this.read)
        {
            LOGGER.error("setHeader(): Cannot use function when in ReadOnly mode.");
            return null;
        }

        if (list.isEmpty())
        {
            LOGGER.error("setHeader(): List parameter is empty!");
            return null;
        }

        if (this.header != null)
        {
            LOGGER.debug("setHeader(): Emptying existing header.");
            this.header.clear();
        }

        LOGGER.debug("setHeader(): Building new Header ...");
        this.header = new HashMap<>();

        for (int i = 0; i < list.size(); i++)
        {
            this.header.put(i, list.get(i));
        }

        this.columns = list.size();

        return new CSVHeader(list);
    }

    public int getSize()
    {
        return this.lines.size();
    }

    public boolean isEmpty()
    {
        return this.getSize() < 1 || this.header.isEmpty();
    }

    public HashMap<Integer, List<String>> getAllLines()
    {
        return this.lines;
    }

    private List<String> truncateLine(List<String> list)
    {
        List<String> entry = new ArrayList<>(list);

        if (!this.header.isEmpty() && entry.size() > this.columns)
        {
            LOGGER.warn("truncateLine(): Truncating line from [{}] -> [{}]", entry.size(), this.columns);

            while (entry.size() > this.columns)
            {
                entry.removeLast();
            }
        }

        return entry;
    }

    public boolean putAllLines(@Nonnull HashMap<Integer, List<String>> mapIn, boolean hasHeader)
    {
        return this.putAllLines(mapIn, hasHeader, -1);
    }

    public boolean putAllLines(@Nonnull HashMap<Integer, List<String>> mapIn, boolean hasHeader, int startAt)
    {
        if (this.read)
        {
            LOGGER.error("putAllLines(): Cannot use function when in ReadOnly mode.");
            return false;
        }

        if (mapIn.isEmpty())
        {
            LOGGER.error("putAllLines(): HashMap parameter is empty!");
            return false;
        }

        if (!this.lines.isEmpty())
        {
            LOGGER.debug("putAllLines(): Emptying existing data.");
            this.lines.clear();
        }

        LOGGER.debug("putAllLines(): Copying [{}] lines... (Start At: {})", mapIn.size(), startAt);
        int line = Math.max(startAt, 0);

        for (int i = line; i < mapIn.size(); i++)
        {
            List<String> entry = this.hasHeader() ? this.truncateLine(mapIn.get(i)) : mapIn.get(i);

            if (entry.isEmpty())
            {
                LOGGER.warn("putAllLines(): LINE[{}] in given parameter is Empty.  Skipping Input line.", i, line);
            }
            else
            {
                if (i == 0 && hasHeader)
                {
                    LOGGER.debug("putAllLines(): Adding Header from line [{}]...", i);
                    this.setHeader(entry);
                }

//                LOGGER.debug("putAllLines(): IN[{}]: Appending ... LINE[{}]: {}", i, line, entry.toString());
                this.lines.put(line, entry);
                line++;
            }
        }

        LOGGER.debug("putAllLines(): Done writing [{}/{}] lines.", this.lines.size(), mapIn.size());
        return true;
    }

    public boolean putLine(@Nonnull List<String> list)
    {
        return this.putLine(list, -1, false);
    }

    public boolean putLine(@Nonnull List<String> list, int line)
    {
        return this.putLine(list, line, false);
    }

    public boolean putLine(@Nonnull List<String> list, int line, boolean replace)
    {
        if (this.read)
        {
            LOGGER.error("putLine(): Cannot use function when in ReadOnly mode.");
            return false;
        }

        if (list.isEmpty())
        {
            LOGGER.error("putLine(): List parameter is Empty!");
            return false;
        }

        // Replace
        if (line > 0)
        {
            if (!replace && this.lines.containsKey(line))
            {
                LOGGER.error("putLine(): A line parameter is specified [{}]; when ReplaceMode is false, and it is not empty.", line);
                return false;
            }

            if (line > this.lines.size())
            {
                LOGGER.error("putLine(): A line parameter is specified [{}]; but the value given exceeds the data size [{}].", line, this.lines.size());
                return false;
            }

            List<String> entry = this.hasHeader() ? this.truncateLine(new ArrayList<>(list)) : new ArrayList<>(list);

            LOGGER.debug("putLine(): Writing ... LINE[{}]: {}", line, entry.toString());
            this.lines.replace(line, entry);

            return true;
        }

        // Append
        for (int i = 0; i < this.lines.size(); i++)
        {
            List<String> entry = this.lines.get(i);
            line = i;

            if (entry.isEmpty())
            {
                break;
            }
        }

        line++;
        LOGGER.debug("putLine(): Appending ... LINE[{}]: {}", line, list.toString());
        List<String> entry = this.hasHeader() ? this.truncateLine(new ArrayList<>(list)) : new ArrayList<>(list);
        this.lines.put(line, entry);

        return true;
    }

    public @Nullable List<String> getLine(int l)
    {
        if (this.lines.isEmpty())
        {
            LOGGER.error("getLine(): File not read!");
            return null;
        }
        else if (this.lines.size() < l)
        {
            LOGGER.error("getLine(): line [{}] does not exist!", l);
            return null;
        }

        if (!this.lines.containsKey(l))
        {
            LOGGER.error("getLine(): line [{}] Not found!", l);
            return null;
        }

        List<String> line = this.lines.get(l);

        if (line == null)
        {
            LOGGER.error("getLine(): line [{}] is empty!", l);
            return null;
        }

        return line;
    }

    @Override
    public void close() throws Exception
    {
        if (this.reader != null)
        {
            this.reader.close();
            this.reader = null;
        }

        if (this.writer != null)
        {
            this.writer.close();
            this.writer = null;
        }

        this.parser = null;
        this.lines.clear();
        this.header.clear();
    }
}
