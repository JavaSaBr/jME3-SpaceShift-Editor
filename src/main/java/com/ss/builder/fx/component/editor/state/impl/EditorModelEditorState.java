package com.ss.builder.fx.component.editor.state.impl;

import static com.ss.builder.config.DefaultSettingsProvider.Preferences.PREF_CAMERA_LAMP;
import static com.ss.builder.config.DefaultSettingsProvider.Defaults.PREF_DEFAULT_CAMERA_LIGHT;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.config.DefaultSettingsProvider;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.fx.component.editor.impl.model.ModelFileEditor;

/**
 * The implementation of a state container for the {@link ModelFileEditor}.
 *
 * @author JavaSaBr
 */
public class EditorModelEditorState extends BaseEditorSceneEditorState {

    /**
     * The constant serialVersionUID.
     */
    public static final long serialVersionUID = 5;

    /**
     * The sky type.
     */
    private volatile int skyType;

    /**
     * Is enabled light.
     */
    private volatile boolean enableLight;

    /**
     * Is enabled physics.
     */
    private volatile boolean enablePhysics;

    /**
     * Is enabled debug physics.
     */
    private volatile boolean enableDebugPhysics;

    public EditorModelEditorState() {
        this.skyType = 0;
        this.enableLight = EDITOR_CONFIG.getBoolean(DefaultSettingsProvider.Preferences.PREF_CAMERA_LAMP, DefaultSettingsProvider.Defaults.PREF_DEFAULT_CAMERA_LIGHT);
        this.enablePhysics = false;
        this.enableDebugPhysics = false;
    }

    /**
     * Gets sky type.
     *
     * @return the sky type.
     */
    @FxThread
    public int getSkyType() {
        return skyType;
    }

    /**
     * Sets sky type.
     *
     * @param skyType the sky type.
     */
    @FxThread
    public void setSkyType(final int skyType) {
        final boolean changed = getSkyType() != skyType;
        this.skyType = skyType;
        if (changed) notifyChange();
    }

    /**
     * Sets enable light.
     *
     * @param enableLight true if the light is enabled.
     */
    @FxThread
    public void setEnableLight(final boolean enableLight) {
        final boolean changed = isEnableLight() != enableLight;
        this.enableLight = enableLight;
        if (changed) notifyChange();
    }

    /**
     * Is enable light boolean.
     *
     * @return true if the light is enabled.
     */
    @FxThread
    public boolean isEnableLight() {
        return enableLight;
    }

    /**
     * @return true if physics is enabled.
     */
    @FxThread
    public boolean isEnablePhysics() {
        return enablePhysics;
    }

    /**
     * @param enablePhysics true if physics is enabled.
     */
    @FxThread
    public void setEnablePhysics(final boolean enablePhysics) {
        final boolean changed = isEnablePhysics() != enablePhysics;
        this.enablePhysics = enablePhysics;
        if (changed) notifyChange();
    }

    /**
     * @return true if debug physics is enabled.
     */
    @FxThread
    public boolean isEnableDebugPhysics() {
        return enableDebugPhysics;
    }

    /**
     * @param enableDebugPhysics true if debug physics is enabled.
     */
    @FxThread
    public void setEnableDebugPhysics(final boolean enableDebugPhysics) {
        final boolean changed = isEnableDebugPhysics() != enableDebugPhysics;
        this.enableDebugPhysics = enableDebugPhysics;
        if (changed) notifyChange();
    }

    @Override
    public String toString() {
        return "EditorModelEditorState{" +
                "skyType=" + skyType +
                ", enableLight=" + enableLight +
                "} " + super.toString();
    }
}
