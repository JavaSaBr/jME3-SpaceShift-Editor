package com.ss.builder.fx.control.property.impl;

import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.dialog.node.selector.NodeSelectorDialog;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.dialog.node.selector.NodeSelectorDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link ElementPropertyControl} to edit elements from models.
 *
 * @param <D> the type of an editing object.
 * @param <T> the type of an editing property.
 * @author JavaSaBr
 */
public abstract class ElementModelPropertyControl<D, T> extends ElementPropertyControl<ModelChangeConsumer, D, T> {

    public ElementModelPropertyControl(
            @NotNull Class<T> type,
            @Nullable T propertyValue,
            @NotNull String propertyName,
            @NotNull ModelChangeConsumer changeConsumer
    ) {
        super(type, propertyValue, propertyName, changeConsumer);
    }

    @Override
    @FxThread
    protected void addElement() {
        createNodeSelectorDialog().show(this);
    }

    /**
     * Create node selector dialog node selector dialog.
     *
     * @return the node selector dialog.
     */
    @FxThread
    protected @NotNull NodeSelectorDialog<T> createNodeSelectorDialog() {
        return new NodeSelectorDialog<>(getChangeConsumer().getCurrentModel(), type, this::addElement);
    }

    /**
     * Process of adding the new element.
     *
     * @param newElement the new element.
     */
    @FxThread
    protected void addElement(@NotNull T newElement) {
        changed(newElement, getPropertyValue());
    }
}
