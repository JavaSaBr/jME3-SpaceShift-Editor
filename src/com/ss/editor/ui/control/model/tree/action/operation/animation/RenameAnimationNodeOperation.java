package com.ss.editor.ui.control.model.tree.action.operation.animation;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;

import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link AbstractEditorOperation} for editing name of an animation.
 *
 * @author JavaSaBr
 */
public class RenameAnimationNodeOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The old name.
     */
    private final String oldName;

    /**
     * The new name.
     */
    private final String newName;

    /**
     * The animation control.
     */
    private final AnimControl control;

    public RenameAnimationNodeOperation(@NotNull final String oldName, @NotNull final String newName,
                                        @NotNull final AnimControl control) {
        this.oldName = oldName;
        this.newName = newName;
        this.control = control;
    }

    @Override
    protected void redoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addEditorThreadTask(() -> {
            final Animation anim = control.getAnim(oldName);
            control.changeAnimName(oldName, newName);
            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyChangeProperty(control, anim, "name"));
        });
    }

    @Override
    protected void undoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addEditorThreadTask(() -> {
            final Animation anim = control.getAnim(newName);
            control.changeAnimName(newName, oldName);
            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyChangeProperty(control, anim, "name"));
        });
    }
}
