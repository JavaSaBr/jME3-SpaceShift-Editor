package com.ss.editor.ui.control.filter.property.control;

import com.jme3.light.DirectionalLight;
import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.ui.control.model.tree.dialog.LightSelectorDialog;
import com.ss.editor.ui.control.model.tree.dialog.NodeSelectorDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.scene.control.Label;

/**
 * The implementation of the {@link AbstractElementFilterPropertyControl} to edit direction light from a scene.
 *
 * @author JavaSaBr
 */
public class DirectionLightElementPropertyControl<D> extends AbstractElementFilterPropertyControl<D, DirectionalLight> {

    public DirectionLightElementPropertyControl(@Nullable final DirectionalLight propertyValue,
                                                @NotNull final String propertyName,
                                                @NotNull final SceneChangeConsumer changeConsumer) {
        super(DirectionalLight.class, propertyValue, propertyName, changeConsumer);
    }

    @NotNull
    protected NodeSelectorDialog<DirectionalLight> createNodeSelectorDialog() {
        final SceneChangeConsumer changeConsumer = getChangeConsumer();
        return new LightSelectorDialog<>(changeConsumer.getCurrentModel(), type, this::processAdd);
    }

    @Override
    protected void reload() {
        final DirectionalLight light = getPropertyValue();
        final Label elementLabel = getElementLabel();
        elementLabel.setText(light == null ? NO_ELEMENT : light.getClass().getSimpleName());
    }
}
