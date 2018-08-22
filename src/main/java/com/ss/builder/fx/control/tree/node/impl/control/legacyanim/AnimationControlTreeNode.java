package com.ss.builder.fx.control.tree.node.impl.control.legacyanim;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.model.ModelNodeTree;
import com.ss.builder.fx.control.tree.action.impl.animation.PlaySettingsAction;
import com.ss.builder.Messages;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.node.impl.control.ControlTreeNode;
import com.ss.builder.fx.control.model.ModelNodeTree;
import com.ss.builder.fx.control.tree.action.impl.animation.PlaySettingsAction;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;

import java.util.Collection;

/**
 * The implementation of node to show {@link AnimControl}.
 *
 * @author JavaSaBr
 */
@Deprecated
public class AnimationControlTreeNode extends ControlTreeNode<AnimControl> {

    /**
     * The loop mode.
     */
    @NotNull
    private LoopMode loopMode;

    /**
     * The animation speed.
     */
    private float speed;

    public AnimationControlTreeNode(@NotNull AnimControl element, long objectId) {
        super(element, objectId);
        this.loopMode = LoopMode.Loop;
        this.speed = 1.0F;
    }

    @Override
    @FxThread
    public void fillContextMenu(@NotNull NodeTree<?> nodeTree, @NotNull ObservableList<MenuItem> items) {
        items.add(new PlaySettingsAction(nodeTree, this));
        super.fillContextMenu(nodeTree, items);
    }

    /**
     * Update settings.
     *
     * @param loopMode the loop mode.
     * @param speed    the animation speed.
     */
    @FxThread
    public void updateSettings(@NotNull LoopMode loopMode, float speed) {
        this.loopMode = loopMode;
        this.speed = speed;
    }

    /**
     * Gets speed.
     *
     * @return the animation speed.
     */
    @FxThread
    public float getSpeed() {
        return speed;
    }

    /**
     * Gets loop mode.
     *
     * @return the loop mode.
     */
    @FxThread
    public @NotNull LoopMode getLoopMode() {
        return loopMode;
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_ANIM_CONTROL;
    }

    @Override
    public @Nullable Image getIcon() {
        return Icons.ANIMATION_16;
    }

    @Override
    @FxThread
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }

    @Override
    @FxThread
    public @NotNull Array<TreeNode<?>> getChildren(@NotNull NodeTree<?> nodeTree) {

        var result = ArrayFactory.<TreeNode<?>>newArray(TreeNode.class);

        var animControl = getElement();
        var animationNames = animControl.getAnimationNames();
        animationNames.forEach(name -> result.add(FACTORY_REGISTRY.createFor(animControl.getAnim(name))));

        result.addAll(super.getChildren(nodeTree));

        return result;
    }

    @Override
    @FxThread
    public void notifyChildPreAdd(@NotNull TreeNode<?> treeNode) {

        var animationModelNode = (AnimationTreeNode) treeNode;
        animationModelNode.setControl(getElement());
        animationModelNode.setControlModelNode(this);

        super.notifyChildPreAdd(treeNode);
    }
}
