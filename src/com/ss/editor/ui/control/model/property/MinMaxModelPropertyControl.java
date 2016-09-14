package com.ss.editor.ui.control.model.property;

import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;

import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link Vector2fModelPropertyControl} for editing min-max value range.
 *
 * @author JavaSaBr.
 */
public class MinMaxModelPropertyControl<T extends Spatial> extends Vector2fModelPropertyControl<T> {

    public MinMaxModelPropertyControl(final Vector2f element, final String paramName, final ModelChangeConsumer modelChangeConsumer) {
        super(element, paramName, modelChangeConsumer);
    }

    @NotNull
    @Override
    protected String getXLabelText() {
        return "Min";
    }

    @NotNull
    @Override
    protected String getYLabelText() {
        return "Max";
    }
}
