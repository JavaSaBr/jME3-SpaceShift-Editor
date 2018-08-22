package com.ss.builder.fx.control.tree.action.impl.particle.emitter.influencer;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.model.undo.impl.emitter.ChangeParticleInfluencerOperation;
import com.ss.builder.fx.Icons;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.undo.editor.ModelChangeConsumer;
import com.ss.builder.fx.Icons;
import com.ss.builder.model.undo.impl.emitter.ChangeParticleInfluencerOperation;
import com.ss.builder.fx.control.tree.NodeTree;
import com.ss.builder.fx.control.tree.action.AbstractNodeAction;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to create a {@link ParticleInfluencer} for a {@link ParticleEmitter}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateParticleInfluencerAction extends AbstractNodeAction<ModelChangeConsumer> {

    public AbstractCreateParticleInfluencerAction(@NotNull final NodeTree<?> nodeTree,
                                                  @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.INFLUENCER_16;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());

        final TreeNode<?> treeNode = getNode();
        final ParticleEmitter emitter = (ParticleEmitter) treeNode.getElement();
        final ParticleInfluencer influencer = createInfluencer();

        changeConsumer.execute(new ChangeParticleInfluencerOperation(influencer, emitter));
    }

    /**
     * Create influencer particle influencer.
     *
     * @return the particle influencer
     */
    @FxThread
    protected abstract @NotNull ParticleInfluencer createInfluencer();
}
