package com.ss.builder.fx.control.tree.node.impl.physics.shape;

import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.fx.Icons;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.fx.Icons;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of node to show {@link PlaneCollisionShape}.
 *
 * @author JavaSaBr
 */
public class PlaneCollisionShapeTreeNode extends CollisionShapeTreeNode<PlaneCollisionShape> {

    public PlaneCollisionShapeTreeNode(@NotNull final PlaneCollisionShape element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.PLANE_16;
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_PLANE_COLLISION_SHAPE;
    }
}
