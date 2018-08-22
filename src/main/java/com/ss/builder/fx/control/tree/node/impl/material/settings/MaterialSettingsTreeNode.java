package com.ss.builder.fx.control.tree.node.impl.material.settings;

import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.node.material.MaterialSettings;
import com.ss.builder.fx.Icons;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.model.node.material.MaterialSettings;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base presentation of material settings in the {@link com.ss.editor.ui.control.tree.NodeTree}.
 *
 * @param <T> the type of material settings.
 * @author JavaSaBr
 */
public class MaterialSettingsTreeNode<T extends MaterialSettings> extends TreeNode<T> {

    public MaterialSettingsTreeNode(@NotNull final T element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.INFLUENCER_16;
    }
}
