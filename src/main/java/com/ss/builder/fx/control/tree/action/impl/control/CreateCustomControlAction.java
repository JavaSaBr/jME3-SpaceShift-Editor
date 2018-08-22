package com.ss.builder.fx.control.tree.action.impl.control;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.dialog.CreateCustomControlDialog;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.action.AbstractNodeAction;
import com.ss.builder.fx.dialog.CreateCustomControlDialog;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to create a {@link Control}.
 *
 * @author JavaSaBr
 */
public class CreateCustomControlAction extends AbstractNodeAction<ModelChangeConsumer> {

    /**
     * Instantiates a new Create custom control action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public CreateCustomControlAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @FxThread
    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.GEAR_16;
    }

    @FxThread
    @NotNull
    @Override
    protected String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_ADD_CONTROL_CUSTOM;
    }

    @FxThread
    @Override
    protected void process() {
        super.process();

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer consumer = notNull(nodeTree.getChangeConsumer());

        final TreeNode<?> treeNode = getNode();
        final Spatial parent = (Spatial) treeNode.getElement();

        final CreateCustomControlDialog dialog = new CreateCustomControlDialog(consumer, parent);
        dialog.show();
    }
}