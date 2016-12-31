package com.ss.editor.ui.control.model.tree.node.control.anim;

import static com.ss.editor.ui.control.model.tree.node.ModelNodeFactory.createFor;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.animation.PlaySettingsAction;
import com.ss.editor.ui.control.model.tree.node.ModelNode;
import com.ss.editor.ui.control.model.tree.node.control.ControlModelNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The implementation of node for showing {@link AnimControl}.
 *
 * @author JavaSaBr
 */
public class AnimationControlModelNode extends ControlModelNode<AnimControl> {

    /**
     * The loop mode.
     */
    @NotNull
    private LoopMode loopMode;

    /**
     * The animation speed.
     */
    private float speed;

    public AnimationControlModelNode(final AnimControl element, final long objectId) {
        super(element, objectId);
        this.loopMode = LoopMode.Loop;
        this.speed = 1.0F;
    }

    @Override
    public void fillContextMenu(@NotNull final ModelNodeTree nodeTree, @NotNull final ObservableList<MenuItem> items) {
        items.add(new PlaySettingsAction(nodeTree, this));
        super.fillContextMenu(nodeTree, items);
    }

    /**
     * Update settings.
     *
     * @param loopMode the loop mode.
     * @param speed    the animation speed.
     */
    public void updateSettings(@NotNull LoopMode loopMode, final float speed) {
        this.loopMode = loopMode;
        this.speed = speed;
    }

    /**
     * @return the animation speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @return the loop mode.
     */
    @NotNull
    public LoopMode getLoopMode() {
        return loopMode;
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_ANIM_CONTROL;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.ANIMATION_16;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @NotNull
    @Override
    public Array<ModelNode<?>> getChildren() {

        final Array<ModelNode<?>> result = ArrayFactory.newArray(ModelNode.class);

        final AnimControl element = getElement();
        final Collection<String> animationNames = element.getAnimationNames();
        animationNames.forEach(name -> result.add(createFor(element.getAnim(name))));

        result.addAll(super.getChildren());

        return result;
    }

    @Override
    public void notifyChildPreAdd(@NotNull final ModelNode<?> modelNode) {

        final AnimationModelNode animationModelNode = (AnimationModelNode) modelNode;
        animationModelNode.setControl(getElement());
        animationModelNode.setControlModelNode(this);

        super.notifyChildPreAdd(modelNode);
    }
}
