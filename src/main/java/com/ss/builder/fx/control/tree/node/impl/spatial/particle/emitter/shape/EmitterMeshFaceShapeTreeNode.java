package com.ss.builder.fx.control.tree.node.impl.spatial.particle.emitter.shape;

import com.jme3.effect.shapes.EmitterMeshFaceShape;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FromAnyThread;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link EmitterShapeTreeNode} for representing the {@link EmitterMeshFaceShape} in the editor.
 *
 * @author JavaSaBr
 */
public class EmitterMeshFaceShapeTreeNode extends EmitterShapeTreeNode {

    public EmitterMeshFaceShapeTreeNode(@NotNull final EmitterMeshFaceShape element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_PARTICLE_EMITTER_SHAPE_MESH_FACE;
    }
}
