package com.ss.editor.ui.control.property.impl;

import static java.lang.Math.min;

import com.jme3.math.ColorRGBA;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.control.property.AbstractPropertyControl;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.util.UIUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import com.ss.rlib.function.SixObjectConsumer;
import com.ss.rlib.ui.util.FXUtils;

/**
 * The implementation of the {@link AbstractPropertyControl} to edit a color values.
 *
 * @author JavaSaBr
 */
public abstract class AbstractColorPropertyControl<C extends ChangeConsumer, T> extends AbstractPropertyControl<C, T, ColorRGBA> {

    /**
     * The color picker.
     */
    private ColorPicker colorPicker;

    public AbstractColorPropertyControl(@Nullable final ColorRGBA propertyValue, @NotNull final String propertyName,
                                        @NotNull final C changeConsumer,
                                        @NotNull final SixObjectConsumer<C, T, String, ColorRGBA, ColorRGBA, BiConsumer<T, ColorRGBA>> changeHandler) {
        super(propertyValue, propertyName, changeConsumer, changeHandler);
    }

    @Override
    protected void createComponents(@NotNull final HBox container) {
        super.createComponents(container);

        colorPicker = new ColorPicker();
        colorPicker.setId(CSSIds.ABSTRACT_PARAM_CONTROL_COLOR_PICKER);
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> updateValue());
        colorPicker.prefWidthProperty().bind(widthProperty().multiply(CONTROL_WIDTH_PERCENT));

        FXUtils.addClassTo(colorPicker, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addToPane(colorPicker, container);
    }

    @Override
    protected boolean isSingleRow() {
        return true;
    }

    /**
     * @return the color picker.
     */
    private ColorPicker getColorPicker() {
        return colorPicker;
    }

    @Override
    protected void reload() {

        final ColorRGBA element = getPropertyValue();
        final ColorPicker colorPicker = getColorPicker();

        if (element == null) {
            colorPicker.setValue(null);
        } else {

            final float red = min(element.getRed(), 1F);
            final float green = min(element.getGreen(), 1F);
            final float blue = min(element.getBlue(), 1F);
            final float alpha = min(element.getAlpha(), 1F);

            colorPicker.setValue(new Color(red, green, blue, alpha));
        }
    }

    /**
     * Updating value.
     */
    private void updateValue() {
        if (isIgnoreListener()) return;

        final ColorPicker colorPicker = getColorPicker();
        final ColorRGBA newColor = UIUtils.convertColor(colorPicker.getValue());
        final ColorRGBA oldValue = getPropertyValue();

        changed(newColor, oldValue == null ? null : oldValue.clone());
    }
}
