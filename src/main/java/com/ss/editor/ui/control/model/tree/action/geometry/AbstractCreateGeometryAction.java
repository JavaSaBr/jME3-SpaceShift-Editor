package com.ss.editor.ui.control.model.tree.action.geometry;

import static java.util.Objects.requireNonNull;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.AddChildOperation;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;

import org.jetbrains.annotations.NotNull;

/**
 * The action to create the {@link Geometry}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateGeometryAction extends AbstractNodeAction<ModelChangeConsumer> {

    /**
     * Instantiates a new Abstract create geometry action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public AbstractCreateGeometryAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @FXThread
    @Override
    protected void process() {
        super.process();

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer consumer = requireNonNull(nodeTree.getChangeConsumer());
        final AssetManager assetManager = EDITOR.getAssetManager();

        final Geometry geometry = createGeometry();
        geometry.setMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"));

        final TreeNode<?> treeNode = getNode();
        final Node parent = (Node) treeNode.getElement();

        consumer.execute(new AddChildOperation(geometry, parent));
    }

    /**
     * Create geometry geometry.
     *
     * @return the geometry
     */
    @NotNull
    protected abstract Geometry createGeometry();
}
