package com.ss.editor.ui.control.model.tree.action.particle.emitter.toneg0d.influerencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;

import org.jetbrains.annotations.NotNull;

import tonegod.emitter.Messages;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.ImpulseInfluencer;

/**
 * The action to create a {@link ImpulseInfluencer} for a {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class CreateImpulseParticleInfluencerAction extends AbstractCreateParticleInfluencerAction {

    /**
     * Instantiates a new Create impulse particle influencer action.
     *
     * @param nodeTree the node tree
     * @param node     the node
     */
    public CreateImpulseParticleInfluencerAction(@NotNull final NodeTree<ModelChangeConsumer> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.PARTICLE_INFLUENCER_IMPULSE;
    }

    @NotNull
    @Override
    protected ParticleInfluencer createInfluencer() {
        return new ImpulseInfluencer();
    }
}
