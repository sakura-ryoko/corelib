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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sakuraryoko.corelib.api.log.AnsiLogger;

public class CSVHeader implements AutoCloseable
{
    private final AnsiLogger LOGGER = new AnsiLogger(this.getClass());
    private final AbstractList<String> headers;

    public CSVHeader()
    {
        this(null);
    }

    public CSVHeader(@Nullable List<String> headers)
    {
        if (headers != null)
        {
            this.headers = new ArrayList<>(headers);
        }
        else
        {
            this.headers = new ArrayList<>();
        }
    }

    public void setDebug(boolean debug, boolean ansiColor)
    {
        LOGGER.toggleDebug(debug);
        LOGGER.toggleAnsiColor(ansiColor);
    }

    public static CSVHeader copy(CSVHeader other)
    {
        return new CSVHeader(other.headers);
    }

    public void setHeaders(@Nonnull List<String> headers)
    {
        this.headers.clear();
        this.headers.addAll(headers);
    }

    public boolean matches(@Nonnull CSVHeader otherHeaders)
    {
        if (this.size() != otherHeaders.size())
        {
            LOGGER.error("matches(): Mismatched sizes: [{} != {}]", this.size(), otherHeaders.size());
            return false;
        }

        Iterator<String> iter = this.iterator();
        Iterator<String> otherIter = otherHeaders.iterator();

        while (iter.hasNext() && otherIter.hasNext())
        {
            String left = iter.next();
            String right = otherIter.next();

            if (!left.matches(right))
            {
                return false;
            }
        }

        return true;
    }

    public CSVHeader add(String header)
    {
        this.headers.add(header);
        return this;
    }

    public int getId(String header)
    {
        for (int i = 0; i < this.headers.size(); i++)
        {
            if (this.headers.get(i).equals(header))
            {
                return i;
            }
        }

        return -1;
    }

    public @Nullable String getFromId(int id)
    {
        if (id >= this.size())
        {
            return null;
        }

        return this.headers.get(id);
    }

    public Stream<String> stream()
    {
        return this.headers.stream();
    }

    public Iterator<String> iterator()
    {
        return this.headers.iterator();
    }

    public int size()
    {
        return this.headers.size();
    }

    public boolean isEmpty()
    {
        return this.headers.isEmpty();
    }

    public void clear()
    {
        if (!this.headers.isEmpty())
        {
            this.headers.clear();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("CSVHeader[");

        this.headers.forEach((entry) ->
        {
            builder.append(entry);
            builder.append(",");
        });
        builder.append("]");
        String result = builder.toString();
        return result.replace(",]", "]");
    }

    @Override
    public void close() throws Exception
    {
        this.clear();
    }
}
