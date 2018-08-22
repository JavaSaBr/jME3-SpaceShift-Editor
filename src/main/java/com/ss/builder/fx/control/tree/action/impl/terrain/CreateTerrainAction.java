package com.ss.builder.fx.control.tree.action.impl.terrain;

import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.action.AbstractNodeAction;
import com.ss.builder.fx.dialog.terrain.CreateTerrainDialog;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to create terrain.
 *
 * @author JavaSaBr
 */
public class CreateTerrainAction extends AbstractNodeAction<ModelChangeConsumer> {

    public CreateTerrainAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.TERRAIN_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_ADD_TERRAIN;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();
        final CreateTerrainDialog dialog = new CreateTerrainDialog(getNode(), getNodeTree());
        dialog.show();
    }
}
