package com.ss.editor.ui.component.editor.impl.model;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.editor.util.EditorUtil.toAssetPath;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.asset.TextureKey;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.manager.ResourceManager;
import com.ss.editor.part3d.editor.impl.model.ModelEditor3DPart;
import com.ss.editor.part3d.editor.impl.model.ModelEditorBulletPart;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.component.editor.EditorDescription;
import com.ss.editor.ui.component.editor.impl.AbstractFileEditor;
import com.ss.editor.ui.component.editor.impl.scene.AbstractSceneFileEditor;
import com.ss.editor.ui.component.editor.state.EditorState;
import com.ss.editor.ui.component.editor.state.impl.EditorModelEditorState;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.editor.util.EditorUtil;
import com.ss.editor.util.MaterialUtils;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.fx.util.FXUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * The implementation of the {@link AbstractFileEditor} for working with {@link Spatial}.
 *
 * @author JavaSaBr
 */
public class ModelFileEditor extends AbstractSceneFileEditor<Spatial, ModelEditor3DPart, EditorModelEditorState> {

    @NotNull
    private static final String NO_FAST_SKY = Messages.MODEL_FILE_EDITOR_NO_SKY;

    /**
     * The constant DESCRIPTION.
     */
    @NotNull
    public static final EditorDescription DESCRIPTION = new EditorDescription();

    static {
        DESCRIPTION.setEditorName(Messages.MODEL_FILE_EDITOR_NAME);
        DESCRIPTION.setConstructor(ModelFileEditor::new);
        DESCRIPTION.setEditorId(ModelFileEditor.class.getSimpleName());
        DESCRIPTION.addExtension(FileExtensions.JME_OBJECT);
    }

    @NotNull
    private static final Array<String> FAST_SKY_LIST = ArrayFactory.newArray(String.class);

    static {
        FAST_SKY_LIST.add(NO_FAST_SKY);
        FAST_SKY_LIST.add("graphics/textures/sky/studio.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env1.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env2.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env3.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/env4.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/outside.hdr");
        FAST_SKY_LIST.add("graphics/textures/sky/inside.hdr");
    }

    /**
     * The bullet state.
     */
    @NotNull
    private final ModelEditorBulletPart bulletState;

    /**
     * The list of fast skies.
     */
    @Nullable
    private ComboBox<String> fastSkyComboBox;

    /**
     * The light toggle.
     */
    @Nullable
    private ToggleButton lightButton;

    /**
     * The physics toggle.
     */
    @Nullable
    private ToggleButton physicsButton;

    /**
     * The debug physics toggle.
     */
    @Nullable
    private ToggleButton debugPhysicsButton;

    private ModelFileEditor() {
        super();
        this.bulletState = new ModelEditorBulletPart(getEditor3DPart());
        this.bulletState.setEnabled(false);
        this.bulletState.setDebugEnabled(false);
        this.bulletState.setSpeed(1F);
        addEditor3DPart(bulletState);
    }

    @Override
    @FxThread
    protected @NotNull ModelEditor3DPart create3DEditorPart() {
        return new ModelEditor3DPart(this);
    }

    /**
     * Get the list of fast skies.
     *
     * @return the list of fast skies.
     */
    @FxThread
    private @NotNull ComboBox<String> getFastSkyComboBox() {
        return notNull(fastSkyComboBox);
    }

    /**
     * Get the light toggle.
     *
     * @return the light toggle.
     */
    @FxThread
    private @NotNull ToggleButton getLightButton() {
        return notNull(lightButton);
    }

    /**
     * Get the physics toggle.
     *
     * @return the physics toggle.
     */
    @FxThread
    private @NotNull ToggleButton getPhysicsButton() {
        return notNull(physicsButton);
    }

    /**
     * Get the debug physics button.
     *
     * @return the debug physics button.
     */
    @FxThread
    private @NotNull ToggleButton getDebugPhysicsButton() {
        return notNull(debugPhysicsButton);
    }

    @Override
    @FxThread
    protected void doOpenFile(@NotNull Path file) throws IOException {
        super.doOpenFile(file);

        var assetFile = notNull(getAssetFile(file), "Asset file for " + file + " can't be null.");
        var modelKey = new ModelKey(toAssetPath(assetFile));

        var assetManager = EditorUtil.getAssetManager();
        var model = assetManager.loadAsset(modelKey);

        MaterialUtils.cleanUpMaterialParams(model);

        var editor3DPart = getEditor3DPart();
        editor3DPart.openModel(model);

        handleAddedObject(model);

        setCurrentModel(model);
        setIgnoreListeners(true);
        try {

            getFastSkyComboBox().getSelectionModel()
                    .select(FAST_SKY_LIST.first());

            refreshTree();

        } finally {
            setIgnoreListeners(false);
        }
    }

    @Override
    @FxThread
    protected void loadState() {
        super.loadState();

        final EditorModelEditorState editorState = notNull(getEditorState());

        final ComboBox<String> fastSkyComboBox = getFastSkyComboBox();
        fastSkyComboBox.getSelectionModel().select(editorState.getSkyType());

        final ToggleButton lightButton = getLightButton();
        lightButton.setSelected(editorState.isEnableLight());

        final ToggleButton physicsButton = getPhysicsButton();
        physicsButton.setSelected(editorState.isEnablePhysics());

        final ToggleButton debugPhysicsButton = getDebugPhysicsButton();
        debugPhysicsButton.setSelected(editorState.isEnableDebugPhysics());
    }

    @Override
    @FxThread
    protected @Nullable Supplier<EditorState> getEditorStateFactory() {
        return EditorModelEditorState::new;
    }

    @Override
    @FxThread
    protected void handleAddedObject(@NotNull final Spatial model) {
        super.handleAddedObject(model);

        final ModelEditor3DPart editor3DState = getEditor3DPart();
        final Array<Geometry> geometries = ArrayFactory.newArray(Geometry.class);

        NodeUtils.addGeometry(model, geometries);

        if (!geometries.isEmpty()) {
            geometries.forEach(geometry -> {
                if (geometry.getQueueBucket() == RenderQueue.Bucket.Sky) {
                    editor3DState.addCustomSky(geometry);
                }
            });
        }
    }

    @Override
    @FxThread
    protected void handleRemovedObject(@NotNull final Spatial model) {
        super.handleRemovedObject(model);

        final ModelEditor3DPart editor3DState = getEditor3DPart();
        final Array<Geometry> geometries = ArrayFactory.newArray(Geometry.class);

        NodeUtils.addGeometry(model, geometries);

        if (!geometries.isEmpty()) {
            geometries.forEach(geometry -> {
                if (geometry.getQueueBucket() == RenderQueue.Bucket.Sky) {
                    editor3DState.removeCustomSky(geometry);
                }
            });
        }
    }

    @Override
    @FromAnyThread
    public @NotNull EditorDescription getDescription() {
        return DESCRIPTION;
    }

    @Override
    @FxThread
    protected void createToolbar(@NotNull final HBox container) {
        super.createToolbar(container);

        final Label fastSkyLabel = new Label(Messages.MODEL_FILE_EDITOR_FAST_SKY + ":");

        fastSkyComboBox = new ComboBox<>();
        fastSkyComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> changeFastSky(newValue));

        final ObservableList<String> skyItems = fastSkyComboBox.getItems();
        skyItems.addAll(FAST_SKY_LIST);

        final ResourceManager resourceManager = ResourceManager.getInstance();
        final Array<Path> additionalEnvs = resourceManager.getAdditionalEnvs();
        additionalEnvs.forEach(path -> skyItems.add(path.toString()));

        FXUtils.addToPane(fastSkyLabel, container);
        FXUtils.addToPane(fastSkyComboBox, container);
    }

    @Override
    @FxThread
    protected void createActions(@NotNull final HBox container) {
        super.createActions(container);

        lightButton = new ToggleButton();
        lightButton.setTooltip(new Tooltip(Messages.SCENE_FILE_EDITOR_ACTION_CAMERA_LIGHT));
        lightButton.setGraphic(new ImageView(Icons.LIGHT_16));
        lightButton.setSelected(true);
        lightButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> changeLight(newValue));

        physicsButton = new ToggleButton();
        physicsButton.setTooltip(new Tooltip(Messages.SCENE_FILE_EDITOR_ACTION_PHYSICS));
        physicsButton.setGraphic(new ImageView(Icons.PHYSICS_16));
        physicsButton.setSelected(false);
        physicsButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> changePhysics(newValue));

        debugPhysicsButton = new ToggleButton();
        debugPhysicsButton.setTooltip(new Tooltip(Messages.SCENE_FILE_EDITOR_ACTION_DEBUG_PHYSICS));
        debugPhysicsButton.setGraphic(new ImageView(Icons.DEBUG_16));
        debugPhysicsButton.setSelected(false);
        debugPhysicsButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> changeDebugPhysics(newValue));

        DynamicIconSupport.addSupport(lightButton, physicsButton, debugPhysicsButton);

        FXUtils.addClassTo(lightButton, physicsButton, debugPhysicsButton, CssClasses.FILE_EDITOR_TOOLBAR_BUTTON);
        FXUtils.addToPane(lightButton, physicsButton, debugPhysicsButton, container);
    }

    /**
     * Handle changing a sky.
     */
    @FxThread
    private void changeFastSky(@NotNull final String newSky) {
        if (isIgnoreListeners()) {
            return;
        }

        final ModelEditor3DPart editor3DState = getEditor3DPart();

        if (NO_FAST_SKY.equals(newSky)) {
            editor3DState.changeFastSky(null);
            final EditorModelEditorState editorState = getEditorState();
            if (editorState != null) editorState.setSkyType(0);
            return;
        }

        final AssetManager assetManager = EditorUtil.getAssetManager();

        final TextureKey key = new TextureKey(newSky, true);
        key.setGenerateMips(false);

        final Texture texture = assetManager.loadTexture(key);
        final Spatial newFastSky = SkyFactory.createSky(assetManager, texture, EnvMapType.EquirectMap);

        editor3DState.changeFastSky(newFastSky);

        final ComboBox<String> fastSkyComboBox = getFastSkyComboBox();
        final SingleSelectionModel<String> selectionModel = fastSkyComboBox.getSelectionModel();
        final int selectedIndex = selectionModel.getSelectedIndex();

        final EditorModelEditorState editorState = getEditorState();
        if (editorState != null) {
            editorState.setSkyType(selectedIndex);
        }
    }

    /**
     * Get the bullet state.
     *
     * @return the bullet state.
     */
    @FxThread
    private @NotNull ModelEditorBulletPart getBulletState() {
        return bulletState;
    }

    /**
     * Handle to change enabling of physics.
     */
    @FxThread
    private void changePhysics(@NotNull final Boolean newValue) {
        if (isIgnoreListeners()) {
            return;
        }

        EXECUTOR_MANAGER.addJmeTask(() -> getBulletState().setEnabled(newValue));

        if (editorState != null) {
            editorState.setEnablePhysics(newValue);
        }
    }

    /**
     * Handle to change enabling of physics.
     */
    @FxThread
    private void changeDebugPhysics(@NotNull final Boolean newValue) {
        if (isIgnoreListeners()) {
            return;
        }

        EXECUTOR_MANAGER.addJmeTask(() -> getBulletState().setDebugEnabled(newValue));

        if (editorState != null) {
            editorState.setEnableDebugPhysics(newValue);
        }
    }

    /**
     * Handle changing camera light visibility.
     */
    @FxThread
    private void changeLight(@NotNull final Boolean newValue) {
        if (isIgnoreListeners()) {
            return;
        }

        final ModelEditor3DPart editor3DState = getEditor3DPart();
        editor3DState.updateLightEnabled(newValue);

        if (editorState != null) {
            editorState.setEnableLight(newValue);
        }
    }

    @Override
    @FxThread
    public void notifyFxAddedChild(@NotNull final Object parent, @NotNull final Object added, final int index,
                                   final boolean needSelect) {
        super.notifyFxAddedChild(parent, added, index, needSelect);

        final ModelEditor3DPart editor3DState = getEditor3DPart();

        if (added instanceof Spatial) {

            final Spatial spatial = (Spatial) added;
            final boolean isSky = spatial.getQueueBucket() == RenderQueue.Bucket.Sky;

            if (isSky) {
                editor3DState.addCustomSky(spatial);
                editor3DState.updateLightProbe();
            }
        }

        EXECUTOR_MANAGER.addFxTask(() -> getBulletState().notifyAdded(added));
    }

    @Override
    @FxThread
    public void notifyFxRemovedChild(@NotNull final Object parent, @NotNull final Object removed) {
        super.notifyFxRemovedChild(parent, removed);

        final ModelEditor3DPart editor3DState = getEditor3DPart();

        if (removed instanceof Spatial) {

            final Spatial spatial = (Spatial) removed;
            final boolean isSky = spatial.getQueueBucket() == RenderQueue.Bucket.Sky;

            if (isSky) {
                editor3DState.removeCustomSky(spatial);
                editor3DState.updateLightProbe();
            }
        }

        EXECUTOR_MANAGER.addFxTask(() -> getBulletState().notifyRemoved(removed));
    }

    @Override
    public String toString() {
        return "ModelFileEditor{" +
                "} " + super.toString();
    }
}
