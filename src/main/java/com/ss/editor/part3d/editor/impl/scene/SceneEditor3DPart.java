package com.ss.editor.part3d.editor.impl.scene;

import static com.ss.editor.config.DefaultSettingsProvider.Defaults.PREF_DEFAULT_FXAA_FILTER;
import static com.ss.editor.config.DefaultSettingsProvider.Defaults.PREF_DEFAULT_TONEMAP_FILTER;
import static com.ss.editor.config.DefaultSettingsProvider.Preferences.PREF_FILTER_FXAA;
import static com.ss.editor.config.DefaultSettingsProvider.Preferences.PREF_FILTER_TONEMAP;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.scene.Node;
import com.ss.editor.JmeApplication;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.config.EditorConfig;
import com.ss.editor.extension.scene.SceneNode;
import com.ss.editor.extension.scene.app.state.SceneAppState;
import com.ss.editor.extension.scene.filter.SceneFilter;
import com.ss.editor.ui.component.editor.impl.scene.SceneFileEditor;
import com.ss.editor.util.EditorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link AbstractSceneEditor3DPart} for the {@link SceneFileEditor}.
 *
 * @author JavaSaBr
 */
public class SceneEditor3DPart extends AbstractSceneEditor3DPart<SceneFileEditor, SceneNode> {

    /**
     * The flag of showing light models.
     */
    private boolean lightShowed;

    /**
     * The flag of showing audio models.
     */
    private boolean audioShowed;

    public SceneEditor3DPart(@NotNull final SceneFileEditor fileEditor) {
        super(fileEditor);

        this.lightShowed = true;
        this.audioShowed = true;

        final Node stateNode = getStateNode();
        stateNode.attachChild(getModelNode());
        stateNode.attachChild(getToolNode());
    }

    @Override
    @JmeThread
    protected int getGridSize() {
        return 1000;
    }

    @Override
    @JmeThread
    protected void attachModel(@NotNull final SceneNode model, @NotNull final Node modelNode) {
    }

    @Override
    @JmeThread
    protected void detachPrevModel(@NotNull final Node modelNode, @Nullable final SceneNode currentModel) {
    }

    @Override
    @JmeThread
    public void initialize(@NotNull final AppStateManager stateManager, @NotNull final Application application) {
        super.initialize(stateManager, application);

        final SceneNode currentModel = getCurrentModel();

        if (currentModel != null) {
            getModelNode().attachChild(currentModel);
        }

        final JmeApplication jmeApplication = JmeApplication.getInstance();
        final FXAAFilter fxaaFilter = jmeApplication.getFXAAFilter();
        fxaaFilter.setEnabled(false);

        final ToneMapFilter toneMapFilter = jmeApplication.getToneMapFilter();
        toneMapFilter.setEnabled(false);
    }

    @Override
    @JmeThread
    public void cleanup() {
        super.cleanup();

        final SceneNode currentModel = getCurrentModel();

        if (currentModel != null) {
            getModelNode().detachChild(currentModel);
        }

        final EditorConfig editorConfig = EditorConfig.getInstance();
        final JmeApplication jmeApplication = JmeApplication.getInstance();

        final FXAAFilter fxaaFilter = jmeApplication.getFXAAFilter();
        fxaaFilter.setEnabled(editorConfig.getBoolean(PREF_FILTER_FXAA, PREF_DEFAULT_FXAA_FILTER));

        final ToneMapFilter toneMapFilter = jmeApplication.getToneMapFilter();
        toneMapFilter.setEnabled(editorConfig.getBoolean(PREF_FILTER_TONEMAP, PREF_DEFAULT_TONEMAP_FILTER));
    }

    /**
     * Add a scene app state.
     *
     * @param appState the scene app state.
     */
    @FromAnyThread
    public void addAppState(@NotNull final SceneAppState appState) {
        EXECUTOR_MANAGER.addJmeTask(() -> addAppStateImpl(appState));
    }

    @JmeThread
    private void addAppStateImpl(@NotNull final SceneAppState appState) {
        final AppStateManager stateManager = EditorUtil.getStateManager();
        stateManager.attach(appState);
    }

    /**
     * Remove a scene app state.
     *
     * @param appState the scene app state.
     */
    @FromAnyThread
    public void removeAppState(@NotNull final SceneAppState appState) {
        EXECUTOR_MANAGER.addJmeTask(() -> removeAppStateImpl(appState));
    }

    @JmeThread
    private void removeAppStateImpl(@NotNull final SceneAppState appState) {
        final AppStateManager stateManager = EditorUtil.getStateManager();
        stateManager.detach(appState);
    }

    /**
     * Add a scene filter.
     *
     * @param sceneFilter the scene filter.
     */
    @FromAnyThread
    public void addFilter(@NotNull final SceneFilter sceneFilter) {
        EXECUTOR_MANAGER.addJmeTask(() -> addFilterImpl(sceneFilter));
    }

    @JmeThread
    private void addFilterImpl(@NotNull final SceneFilter sceneFilter) {
        final FilterPostProcessor postProcessor = EditorUtil.getGlobalFilterPostProcessor();
        postProcessor.addFilter(sceneFilter.get());
    }

    /**
     * Remove a scene filter.
     *
     * @param sceneFilter the scene filter.
     */
    @FromAnyThread
    public void removeFilter(@NotNull final SceneFilter sceneFilter) {
        EXECUTOR_MANAGER.addJmeTask(() -> removeFilterImpl(sceneFilter));
    }

    @JmeThread
    private void removeFilterImpl(@NotNull final SceneFilter sceneFilter) {
        final FilterPostProcessor postProcessor = EditorUtil.getGlobalFilterPostProcessor();
        postProcessor.removeFilter(sceneFilter.get());
    }

    /**
     * @return true if need to show light models.
     */
    @JmeThread
    private boolean isLightShowed() {
        return lightShowed;
    }

    /**
     * @param lightShowed true if need to show light models.
     */
    @JmeThread
    private void setLightShowed(final boolean lightShowed) {
        this.lightShowed = lightShowed;
    }

    /**
     * @return true if need to show audio models.
     */
    @JmeThread
    private boolean isAudioShowed() {
        return audioShowed;
    }

    /**
     * @param audioShowed true if need to show audio models.
     */
    @JmeThread
    private void setAudioShowed(final boolean audioShowed) {
        this.audioShowed = audioShowed;
    }

    /**
     * Change light showing.
     *
     * @param showed the showed
     */
    @FromAnyThread
    public void updateLightShowed(final boolean showed) {
        EXECUTOR_MANAGER.addJmeTask(() -> updateLightShowedImpl(showed));
    }

    /**
     * The process to change light showing.
     */
    @JmeThread
    private void updateLightShowedImpl(final boolean showed) {

        if (showed == isLightShowed()) {
            return;
        }

        final Node lightNode = getLightNode();
        final Node modelNode = getModelNode();

        if (showed) {
            modelNode.attachChild(lightNode);
        } else {
            modelNode.detachChild(lightNode);
        }

        setLightShowed(showed);
    }

    /**
     * Change audio showing.
     *
     * @param showed the showed
     */
    @FromAnyThread
    public void updateAudioShowed(final boolean showed) {
        EXECUTOR_MANAGER.addJmeTask(() -> updateAudioShowedImpl(showed));
    }

    /**
     * The process to change audio showing.
     */
    @JmeThread
    private void updateAudioShowedImpl(final boolean showed) {

        if (showed == isAudioShowed()) {
            return;
        }

        final Node audioNode = getAudioNode();
        final Node modelNode = getModelNode();

        if (showed) {
            modelNode.attachChild(audioNode);
        } else {
            modelNode.detachChild(audioNode);
        }

        setAudioShowed(showed);
    }
}
