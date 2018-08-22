package com.ss.builder.fx.control.tree.action.impl;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.scene.Spatial;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ChangeConsumer;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.model.undo.impl.RemoveChildOperation;
import com.ss.builder.fx.Icons;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ChangeConsumer;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.model.undo.impl.RemoveChildOperation;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.action.AbstractNodeAction;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to remove a node from model.
 *
 * @author JavaSaBr
 */
public class RemoveNodeAction extends AbstractNodeAction<ModelChangeConsumer> {

    public RemoveNodeAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_REMOVE;
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.REMOVE_12;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();

        final TreeNode<?> node = getNode();
        final Object element = node.getElement();
        if (!(element instanceof Spatial)) {
            return;
        }

        final Spatial spatial = (Spatial) element;

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new RemoveChildOperation(spatial, spatial.getParent()));
    }
}
