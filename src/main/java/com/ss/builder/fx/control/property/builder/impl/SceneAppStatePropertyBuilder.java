package com.ss.builder.fx.control.property.builder.impl;

import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.SceneChangeConsumer;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.annotation.FxThread;
import com.ss.editor.extension.property.EditableProperty;
import com.ss.editor.extension.scene.app.state.EditableSceneAppState;
import com.ss.builder.model.undo.editor.SceneChangeConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The property builder to build property controls of editable scene app states.
 *
 * @author JavaSaBr
 */
public class SceneAppStatePropertyBuilder extends EditableModelObjectPropertyBuilder {

    private static final SceneAppStatePropertyBuilder INSTANCE = new SceneAppStatePropertyBuilder();

    @FromAnyThread
    public static @NotNull SceneAppStatePropertyBuilder getInstance() {
        return INSTANCE;
    }

    protected SceneAppStatePropertyBuilder() {
        super(SceneChangeConsumer.class);
    }

    @Override
    @FxThread
    protected @Nullable List<EditableProperty<?, ?>> getProperties(@NotNull Object object) {
        if (object instanceof EditableSceneAppState) {
            return ((EditableSceneAppState) object).getEditableProperties();
        } else {
            return null;
        }
    }
}