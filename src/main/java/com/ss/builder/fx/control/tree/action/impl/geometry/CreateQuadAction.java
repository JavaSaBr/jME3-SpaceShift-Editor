package com.ss.builder.fx.control.tree.action.impl.geometry;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.fx.Icons;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.node.TreeNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.scene.image.Image;

/**
 * The action to create a {@link Geometry} with a {@link Quad} mesh.
 *
 * @author JavaSaBr
 */
public class CreateQuadAction extends AbstractCreateGeometryAction {

    public CreateQuadAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.PLANE_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_CREATE_PRIMITIVE_QUAD;
    }

    @Override
    @FxThread
    protected @NotNull Geometry createGeometry() {
        return new Geometry("Quad", new Quad(2, 2));
    }
}
