package com.ss.editor.ui.component.editor.state.impl;

import com.ss.editor.ui.component.editor.state.EditorState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The empty implementation of editor state.
 *
 * @author JavaSaBr
 */
public final class VoidEditorState implements EditorState {

    private VoidEditorState() {
        throw new RuntimeException();
    }

    @Override
    public void setChangeHandler(@NotNull final Runnable handle) {
    }

    @Override
    public <T extends AdditionalEditorState> @NotNull T getOrCreateAdditionalState(@NotNull final Class<T> type,
                                                                                   @NotNull final Supplier<T> factory) {
        throw new RuntimeException();
    }
}