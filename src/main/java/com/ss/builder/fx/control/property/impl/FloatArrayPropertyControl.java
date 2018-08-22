package com.ss.builder.fx.control.property.impl;

import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.model.undo.editor.ChangeConsumer;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.model.undo.editor.ChangeConsumer;
import com.ss.builder.fx.control.property.PropertyControl;
import com.ss.rlib.fx.control.input.FloatArrayTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link PropertyControl} to edit float array values.
 *
 * @param <C> the change consumer's type.
 * @param <D> the type of an editing object.
 * @author JavaSaBr
 */
public class FloatArrayPropertyControl<C extends ChangeConsumer, D>
        extends StringBasedArrayPropertyControl<C, D, float[], FloatArrayTextField> {

    public FloatArrayPropertyControl(
            @Nullable float[] propertyValue,
            @NotNull String propertyName,
            @NotNull C changeConsumer
    ) {
        super(propertyValue, propertyName, changeConsumer);
    }

    public FloatArrayPropertyControl(
            @Nullable float[] propertyValue,
            @NotNull String propertyName,
            @NotNull C changeConsumer,
            @Nullable ChangeHandler<C, D, float[]> changeHandler
    ) {
        super(propertyValue, propertyName, changeConsumer, changeHandler);
    }


    @Override
    @FromAnyThread
    protected @NotNull FloatArrayTextField createFieldControl() {
        return new FloatArrayTextField();
    }
}
