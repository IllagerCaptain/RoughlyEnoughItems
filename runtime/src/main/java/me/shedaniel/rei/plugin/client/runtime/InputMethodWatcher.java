/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
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

package me.shedaniel.rei.plugin.client.runtime;

import me.shedaniel.math.Color;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.search.method.InputMethod;
import me.shedaniel.rei.api.client.search.method.InputMethodRegistry;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.impl.client.config.ConfigManagerInternal;
import me.shedaniel.rei.impl.client.gui.hints.HintProvider;
import me.shedaniel.rei.impl.client.gui.menu.MenuAccess;
import me.shedaniel.rei.impl.client.gui.widget.CraftableFilterButtonWidget;
import me.shedaniel.rei.impl.client.gui.widget.search.OverlaySearchField;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InputMethodWatcher implements HintProvider {
    public static final UUID MENU_UUID = UUID.fromString("b93cc166-d06f-4c5f-9bf0-334d18b4adaf");
    
    @Override
    public List<Component> provide() {
        if (PluginManager.areAnyReloading() || OverlaySearchField.isHighlighting) return Collections.emptyList();
        ResourceLocation id = ConfigObject.getInstance().getInputMethodId();
        if (id == null) {
            String languageCode = Minecraft.getInstance().options.languageCode;
            MutableComponent component = new TextComponent("");
            int match = 0;
            for (Map.Entry<ResourceLocation, InputMethod<?>> entry : InputMethodRegistry.getInstance().getAll().entrySet()) {
                InputMethod<?> method = entry.getValue();
                if (entry.getKey().equals(new ResourceLocation("rei:default"))) continue;
                if (CollectionUtils.anyMatch(method.getMatchingLocales(), locale -> locale.code().equals(languageCode))) {
                    if (!component.getString().isEmpty()) {
                        component.append(", ");
                    }
                    
                    component.append(method.getName());
                    match++;
                }
            }
            if (match > 0) {
                return List.of(new TranslatableComponent("text.rei.input.methods.hint"),
                        new TextComponent(" "), component);
            }
        }
        
        return Collections.emptyList();
    }
    
    @Override
    @Nullable
    public Tooltip provideTooltip(Point mouse) {
        return null;
    }
    
    @Override
    public Color getColor() {
        return Color.ofTransparent(0x50ffadca);
    }
    
    @Override
    public List<HintButton> getButtons(MenuAccess access) {
        return List.of(
                new HintButton(new TranslatableComponent("text.rei.input.methods.hint.configure"), bounds -> {
                    access.openOrClose(MENU_UUID, bounds.clone(),
                            () -> CraftableFilterButtonWidget.createInputMethodEntries(CraftableFilterButtonWidget.getApplicableInputMethods()));
                }),
                new HintButton(new TranslatableComponent("text.rei.input.methods.hint.ignore"), bounds -> {
                    ConfigManagerInternal.getInstance().set("functionality.inputMethod", new ResourceLocation("rei:default"));
                })
        );
    }
}
