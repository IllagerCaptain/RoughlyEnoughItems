/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shedaniel.rei.api.client.gui.widgets;

import me.shedaniel.math.Point;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.impl.ClientInternals;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
@ApiStatus.NonExtendable
public interface TooltipContext {
    static TooltipContext of(Item.TooltipContext vanillaContext) {
        return TooltipContext.of(new Point(), vanillaContext);
    }
    
    static TooltipContext of(Point point, Item.TooltipContext vanillaContext) {
        return TooltipContext.of(point, vanillaContext, null);
    }
    
    static TooltipContext of(Point point, Item.TooltipContext vanillaContext, @Nullable TooltipFlag flag) {
        return TooltipContext.of(point, vanillaContext, flag, false);
    }
    
    static TooltipContext of(Point point, Item.TooltipContext vanillaContext, @Nullable TooltipFlag flag, boolean isSearch) {
        return ClientInternals.createTooltipContext(point, flag, isSearch, vanillaContext);
    }
    
    static TooltipContext ofMouse(Item.TooltipContext vanillaContext) {
        return TooltipContext.of(PointHelper.ofMouse(), vanillaContext);
    }
    
    TooltipFlag getFlag();
    
    Point getPoint();
    
    boolean isSearch();
    
    Item.TooltipContext vanillaContext();
}
