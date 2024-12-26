/*
 * This file is part of the CoreLib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Sakura Ryoko and contributors
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

package com.sakuraryoko.corelib.impl.text;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
//#if MC >= 11902
//#else
import net.minecraft.network.chat.TextComponent;
//#endif

import com.sakuraryoko.corelib.api.text.ITextHandler;

public class BuiltinTextHandler implements ITextHandler
{
    @Override
    public Component formatTextSafe(@Nonnull String s)
    {
        return of(s);
    }

    @Override
    public Component formatText(@Nonnull String s)
    {
        return of(s);
    }

    @Override
    public Component of(@Nonnull String s)
    {
        //#if MC >= 11902
        //$$ return Component.literal(s);
        //#else
        return new TextComponent(s);
        //#endif
    }
}
