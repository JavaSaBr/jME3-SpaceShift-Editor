package com.ss.builder.model.undo.impl;

import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.annotation.JmeThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.annotation.JmeThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link AbstractEditorOperation} to add a {@link Light} to a {@link Node}.
 *
 * @author JavaSaBr
 */
public class AddLightOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The new light.
     */
    @NotNull
    private final Light light;

    /**
     * The parent.
     */
    @NotNull
    private final Node parent;

    public AddLightOperation(@NotNull Light light, @NotNull Node parent) {
        this.light = light;
        this.parent = parent;
    }

    @Override
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        parent.addLight(light);
    }

    @Override
    @FxThread
    protected void endRedoInFx(@NotNull ModelChangeConsumer editor) {
        super.endRedoInFx(editor);
        editor.notifyFxAddedChild(parent, light, -1, true);
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        parent.removeLight(light);
    }

    @Override
    @FxThread
    protected void endUndoInFx(@NotNull ModelChangeConsumer editor) {
        super.endUndoInFx(editor);
        editor.notifyFxRemovedChild(parent, light);
    }
}
