package com.ss.builder.fx.control.tree.action.impl;

import static com.ss.builder.fx.control.tree.NodeTreeCell.DATA_FORMAT;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.action.AbstractNodeAction;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to copy a node in model.
 *
 * @author JavaSaBr
 */
public class CopyNodeAction extends AbstractNodeAction<ModelChangeConsumer> {

    public CopyNodeAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_COPY;
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.COPY_16;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();

        final TreeNode<?> node = getNode();

        final ClipboardContent content = new ClipboardContent();
        content.put(DATA_FORMAT, node.getObjectId());

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(content);
    }
}
