package com.ss.editor.ui.preview;

import com.ss.editor.annotation.FxThread;
import com.ss.rlib.common.util.FileUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.pools.Reusable;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The interface to implement a file preview.
 *
 * @author JavaSaBr
 */
public interface FilePreview extends Reusable {

    /**
     * Initialize this preview to work in the pane.
     *
     * @param pane the pane.
     */
    @FxThread
    void initialize(@NotNull StackPane pane);

    /**
     * Hide this preview.
     */
    @FxThread
    void hide();

    /**
     * Check of supporting of the resource.
     *
     * @param resource the resource.
     * @return true if this preview supports the resource.
     */
    @FxThread
    default boolean isSupport(@NotNull final String resource) {
        return !StringUtils.isEmpty(FileUtils.getExtension(resource));
    }

    /**
     * Check of supporting of the file.
     *
     * @param file the file.
     * @return true if this preview supports the file.
     */
    @FxThread
    default boolean isSupport(@NotNull final Path file) {
        return !StringUtils.isEmpty(FileUtils.getExtension(file));
    }

    /**
     * Show the preview of the resource.
     *
     * @param resource the resource.
     */
    @FxThread
    void show(@NotNull String resource);

    /**
     * Show the preview of the file.
     *
     * @param file the file.
     */
    @FxThread
    void show(@NotNull Path file);

    /**
     * Get the order of this preview component.
     *
     * @return the order.
     */
    @FxThread
    default int getOrder() {
        return 0;
    }

    @Override
    @FxThread
    default void release() {
    }
}
