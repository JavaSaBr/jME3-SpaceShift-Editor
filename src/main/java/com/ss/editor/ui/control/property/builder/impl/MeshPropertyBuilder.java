package com.ss.editor.ui.control.property.builder.impl;

import com.jme3.scene.Mesh;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.impl.DefaultSinglePropertyControl;
import com.ss.editor.ui.control.property.impl.EnumPropertyControl;
import com.ss.rlib.fx.util.FXUtils;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link PropertyBuilder} to build property controls for {@link Mesh} objects.
 *
 * @author JavaSaBr
 */
public class MeshPropertyBuilder extends AbstractPropertyBuilder<ModelChangeConsumer> {

    @NotNull
    private static final Mesh.Mode[] MODES = Mesh.Mode.values();

    @NotNull
    private static final PropertyBuilder INSTANCE = new MeshPropertyBuilder();

    /**
     * Get the single instance.
     *
     * @return the single instance
     */
    @FromAnyThread
    public static @NotNull PropertyBuilder getInstance() {
        return INSTANCE;
    }

    private MeshPropertyBuilder() {
        super(ModelChangeConsumer.class);
    }

    @Override
    @FxThread
    protected void buildForImpl(@NotNull final Object object, @Nullable final Object parent, @NotNull final VBox container,
                                @NotNull final ModelChangeConsumer changeConsumer) {

        if (!(object instanceof Mesh)) return;

        final Mesh mesh = (Mesh) object;
        final Mesh.Mode mode = mesh.getMode();

        final int id = mesh.getId();
        final int instanceCount = mesh.getInstanceCount();
        final int vertexCount = mesh.getVertexCount();
        final int numLodLevels = mesh.getNumLodLevels();
        final int triangleCount = mesh.getTriangleCount();

        final DefaultSinglePropertyControl<ModelChangeConsumer, Mesh, Integer> idControl =
                new DefaultSinglePropertyControl<>(id, Messages.MODEL_PROPERTY_ID, changeConsumer);

        idControl.setSyncHandler(Mesh::getId);
        idControl.setToStringFunction(value -> Integer.toString(value));
        idControl.setEditObject(mesh);

        final DefaultSinglePropertyControl<ModelChangeConsumer, Mesh, Integer> instanceCountControl =
                new DefaultSinglePropertyControl<>(instanceCount, Messages.MODEL_PROPERTY_INSTANCE_COUNT, changeConsumer);

        instanceCountControl.setSyncHandler(Mesh::getInstanceCount);
        instanceCountControl.setToStringFunction(value -> Integer.toString(value));
        instanceCountControl.setEditObject(mesh);

        final DefaultSinglePropertyControl<ModelChangeConsumer, Mesh, Integer> vertexCountControl =
                new DefaultSinglePropertyControl<>(vertexCount, Messages.MODEL_PROPERTY_VERTEX_COUNT, changeConsumer);

        vertexCountControl.setSyncHandler(Mesh::getVertexCount);
        vertexCountControl.setToStringFunction(value -> Integer.toString(value));
        vertexCountControl.setEditObject(mesh);

        final DefaultSinglePropertyControl<ModelChangeConsumer, Mesh, Integer> triangleCountControl =
                new DefaultSinglePropertyControl<>(triangleCount, Messages.MODEL_PROPERTY_TRIANGLE_COUNT, changeConsumer);

        triangleCountControl.setSyncHandler(Mesh::getTriangleCount);
        triangleCountControl.setToStringFunction(value -> Integer.toString(value));
        triangleCountControl.setEditObject(mesh);

        final DefaultSinglePropertyControl<ModelChangeConsumer, Mesh, Integer> numLodLevelsControl =
                new DefaultSinglePropertyControl<>(numLodLevels, Messages.MODEL_PROPERTY_NUM_LOD_LEVELS, changeConsumer);

        numLodLevelsControl.setSyncHandler(Mesh::getNumLodLevels);
        numLodLevelsControl.setToStringFunction(value -> Integer.toString(value));
        numLodLevelsControl.setEditObject(mesh);

        final EnumPropertyControl<ModelChangeConsumer, Mesh, Mesh.Mode> modeControl =
                new EnumPropertyControl<>(mode, Messages.MODEL_PROPERTY_MODE, changeConsumer, MODES);
        modeControl.setApplyHandler(Mesh::setMode);
        modeControl.setSyncHandler(Mesh::getMode);
        modeControl.setEditObject(mesh);

        FXUtils.addToPane(idControl, container);
        FXUtils.addToPane(instanceCountControl, container);
        FXUtils.addToPane(vertexCountControl, container);
        FXUtils.addToPane(triangleCountControl, container);
        FXUtils.addToPane(numLodLevelsControl, container);
        FXUtils.addToPane(modeControl, container);
    }
}
