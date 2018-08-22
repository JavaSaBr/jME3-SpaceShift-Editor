package com.ss.builder.fx.control.tree.node.impl.control;

import com.jme3.animation.SkeletonControl;
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
 * The implementation of node to show {@link SkeletonControl}.
 *
 * @author JavaSaBr
 */
public class SkeletonControlTreeNode extends ControlTreeNode<SkeletonControl> {

    public SkeletonControlTreeNode(@NotNull SkeletonControl element, long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.SKELETON_16;
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_SKELETON_CONTROL;
    }
}
