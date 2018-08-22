package com.ss.builder.util;

import static com.ss.rlib.common.util.ClassUtils.cast;
import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import static java.lang.ThreadLocal.withInitial;
import static java.util.stream.Collectors.toList;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.input.InputManager;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeSystem;
import com.jme3.system.Platform;
import com.ss.builder.JfxApplication;
import com.ss.builder.JmeApplication;
import com.ss.builder.analytics.google.GAnalytics;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.annotation.FxThread;
import com.ss.builder.annotation.JmeThread;
import com.ss.builder.config.EditorConfig;
import com.ss.builder.fx.event.FxEventManager;
import com.ss.builder.fx.event.impl.RequestedOpenFileEvent;
import com.ss.builder.fx.scene.EditorFxScene;
import com.ss.builder.fx.util.UiUtils;
import com.ss.builder.manager.ClasspathManager;
import com.ss.builder.manager.ExecutorManager;
import com.ss.builder.manager.ResourceManager;
import com.ss.builder.model.undo.editor.ChangeConsumer;
import com.ss.builder.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.extension.scene.SceneLayer;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import javafx.application.HostServices;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.StackWalker.Option;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * The class with utility methods for the Editor.
 *
 * @author JavaSaBr
 */
public abstract class EditorUtils {

    private static final Logger LOGGER = LoggerManager.getLogger(EditorUtils.class);

    public static final DataFormat JAVA_PARAM = new DataFormat("jMB.javaParam");
    public static final DataFormat GNOME_FILES = new DataFormat("x-special/gnome-copied-files");

    private static final ThreadLocal<SimpleDateFormat> LOCATE_DATE_FORMAT = withInitial(() ->
            new SimpleDateFormat("HH:mm:ss:SSS"));

    private static final ThreadLocal<ObjectDictionary<Class<?>, Enum<?>[]>> ENUM_VALUES_LOCAL =
            ThreadLocal.withInitial(DictionaryFactory::newObjectDictionary);

    private static JmeApplication jmeApplication;
    private static JfxApplication jfxApplication;

    public static void setJmeApplication(@NotNull JmeApplication jmeApplication) {

        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass();

        if (callerClass != JmeApplication.class) {
            throw new IllegalArgumentException();
        }

        EditorUtils.jmeApplication = jmeApplication;
    }

    public static void setJfxApplication(@NotNull JfxApplication jfxApplication) {

        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass();

        if (callerClass != JfxApplication.class) {
            throw new IllegalArgumentException();
        }

        EditorUtils.jfxApplication = jfxApplication;
    }

    /**
     * Get the asset manager.
     *
     * @return the asset manager.
     */
    @FromAnyThread
    public static @NotNull AssetManager getAssetManager() {
        return jmeApplication.getAssetManager();
    }

    /**
     * Get the input manager.
     *
     * @return the input manager.
     */
    @FromAnyThread
    public static @NotNull InputManager getInputManager() {
        return jmeApplication.getInputManager();
    }

    /**
     * Get the render manager.
     *
     * @return the render manager.
     */
    @FromAnyThread
    public static @NotNull RenderManager getRenderManager() {
        return jmeApplication.getRenderManager();
    }

    /**
     * Get the renderer.
     *
     * @return the renderer.
     */
    @FromAnyThread
    public static @NotNull Renderer getRenderer() {
        return jmeApplication.getRenderer();
    }

    /**
     * Get the root node.
     *
     * @return the root node.
     */
    @JmeThread
    public static @NotNull Node getRootNode(@NotNull Application application) {

        if (application instanceof JmeApplication) {
            return ((JmeApplication) application).getRootNode();
        }

        throw new IllegalArgumentException("The application " + application + " doesn't have a root node.");
    }

    /**
     * Get the root node.
     *
     * @return the root node.
     */
    @JmeThread
    public static @NotNull Node getGlobalRootNode() {
        return jmeApplication.getRootNode();
    }

    /**
     * Get the preview node.
     *
     * @return the preview node.
     */
    @JmeThread
    public static @NotNull Node getPreviewNode() {
        return jmeApplication.getPreviewNode();
    }

    /**
     * Get the preview camera.
     *
     * @return the preview camera.
     */
    @JmeThread
    public static @NotNull Camera getPreviewCamera() {
        return jmeApplication.getPreviewCamera();
    }

    /**
     * Get the camera.
     *
     * @return the camera.
     */
    @FromAnyThread
    public static @NotNull Camera getGlobalCamera() {
        return jmeApplication.getCamera();
    }

    /**
     * Get the global filter post processor.
     *
     * @return the global filter post processor.
     */
    @JmeThread
    public static @NotNull FilterPostProcessor getGlobalFilterPostProcessor() {
        return jmeApplication.getPostProcessor();
    }

    /**
     * Get the default material.
     *
     * @return the default material.
     */
    @FromAnyThread
    public static @NotNull Material getDefaultMaterial() {
        return jmeApplication.getDefaultMaterial();
    }

    /**
     * Disable the global PBR light probe.
     */
    @JmeThread
    public static void disableGlobalLightProbe() {
        jmeApplication.disableLightProbe();
    }

    /**
     * Enable the global PBR light probe.
     */
    @JmeThread
    public static void enableGlobalLightProbe() {
        jmeApplication.enableLightProbe();
    }

    /**
     * Update the light probe.
     *
     * @param progressAdapter the progress adapter
     */
    @JmeThread
    public static void updateGlobalLightProbe(@NotNull JobProgressAdapter<LightProbe> progressAdapter) {
        jmeApplication.updateLightProbe(progressAdapter);
    }

    /**
     * Get the state manager.
     *
     * @return the state manager.
     */
    @FromAnyThread
    public static @NotNull AppStateManager getStateManager() {
        return jmeApplication.getStateManager();
    }

    /**
     * Gets the last opened window.
     *
     * @return the last opened window.
     */
    @FxThread
    public static @NotNull Window getFxLastWindow() {
        return jfxApplication.getLastWindow();
    }

    /**
     * Get the current JavaFX scene.
     *
     * @return the JavaFX scene.
     */
    @FxThread
    public static @NotNull EditorFxScene getFxScene() {
        return jfxApplication.getScene();
    }

    /**
     * Get the host services.
     *
     * @return the host services.
     */
    @FxThread
    public static @NotNull HostServices getHostServices() {
        return jfxApplication.getHostServices();
    }

    /**
     * Get the current stage of JavaFX.
     *
     * @return the current stage of JavaFX.
     */
    @FxThread
    public static @NotNull Stage getFxStage() {
        return jfxApplication.getStage();
    }

    /**
     * Register the opened new window.
     *
     * @param window the opened new window.
     */
    @FromAnyThread
    public static void addFxWindow(@NotNull Window window) {
        jfxApplication.addWindow(window);
    }

    /**
     * Delete the closed window.
     *
     * @param window the closed window.
     */
    @FromAnyThread
    public static void removeFxWindow(@NotNull Window window) {
        jfxApplication.removeWindow(window);
    }

    /**
     * Request focus to FX window.
     */
    @FxThread
    public static void requestFxFocus() {
        jfxApplication.requestFocus();
    }

    /**
     * Added files like files to copy to clipboard content.
     *
     * @param paths   the list of files.
     * @param content the content to store.
     */
    @FxThread
    public static @NotNull ClipboardContent addCopiedFile(
            @NotNull Array<Path> paths,
            @NotNull ClipboardContent content
    ) {

        var files = paths.stream()
                .map(Path::toFile)
                .collect(toList());

        content.putFiles(files);
        content.put(EditorUtils.JAVA_PARAM, "copy");

        var platform = JmeSystem.getPlatform();

        if (platform == Platform.Linux64 || platform == Platform.Linux32) {

            var builder = new StringBuilder("copy\n");

            paths.forEach(builder, (path, b) ->
                    b.append(path.toUri().toASCIIString()).append('\n'));

            builder.delete(builder.length() - 1, builder.length());

            var buffer = ByteBuffer.allocate(builder.length());

            for (int i = 0, length = builder.length(); i < length; i++) {
                buffer.put((byte) builder.charAt(i));
            }

            buffer.flip();

            content.put(GNOME_FILES, buffer);
        }

        return content;
    }

    /**
     * Check exists boolean.
     *
     * @param path the path to resource.
     * @return true if the resource is exists.
     */
    @FromAnyThread
    public static boolean checkExists(@NotNull String path) {
        var cs = EditorUtils.class;
        return cs.getResource(path) != null || cs.getResource("/" + path) != null;
    }

    /**
     * Check exists boolean.
     *
     * @param path        the path to resource.
     * @param classLoader the class loader.
     * @return true if the resource is exists.
     */
    @FromAnyThread
    public static boolean checkExists(@NotNull String path, @NotNull ClassLoader classLoader) {
        return classLoader.getResource(path) != null || classLoader.getResource("/" + path) != null;
    }

    /**
     * Convert classpath path to external path.
     *
     * @param path        the path to resource.
     * @param classLoader the class loader.
     * @return the external form or null.
     */
    @FromAnyThread
    public static @Nullable String toExternal(@NotNull String path, @NotNull ClassLoader classLoader) {

        if (!checkExists(path, classLoader)) {
            return null;
        }

        var resource = classLoader.getResource(path);

        if (resource == null) {
            resource = classLoader.getResource("/" + path);
        }

        return resource == null ? null : resource.toExternalForm();
    }

    /**
     * Get an input stream.
     *
     * @param path the path to resource.
     * @return the input stream of the resource or null.
     */
    @FromAnyThread
    public static @Nullable InputStream getInputStream(@NotNull String path) {
        return JfxApplication.class.getResourceAsStream(path);
    }

    /**
     * Get an input stream or throw an exception.
     *
     * @param path the path to resource.
     * @return the input stream of the resource or null.
     */
    @FromAnyThread
    public static @NotNull InputStream requireInputStream(@NotNull String path) {
        return notNull(JfxApplication.class.getResourceAsStream(path));
    }

    /**
     * Get the input stream.
     *
     * @param path        the path to resource.
     * @param classLoader the class loader.
     * @return the input stream of the resource or null.
     */
    @FromAnyThread
    public static @Nullable InputStream getInputStream(@NotNull String path, @NotNull ClassLoader classLoader) {
        return classLoader.getResourceAsStream(path);
    }

    /**
     * Convert unix time to string presentation.
     *
     * @param time the unix time.
     * @return the string presentation.
     */
    @FromAnyThread
    public static @NotNull String timeFormat(long time) {
        var format = LOCATE_DATE_FORMAT.get();
        return format.format(new Date(time));
    }

    /**
     * Get the path to the file from the asset folder.
     *
     * @param assetFolder the asset folder.
     * @param file        the file.
     * @return the relative path.
     */
    @FromAnyThread
    public static @NotNull Path getAssetFile(@NotNull Path assetFolder, @NotNull Path file) {
        return assetFolder.relativize(file);
    }

    /**
     * Get a relative file to the file from the current asset folder.
     *
     * @param file the file.
     * @return the relative file or null.
     */
    @FromAnyThread
    public static @Nullable Path getAssetFile(@NotNull Path file) {
        try {

            return EditorConfig.getInstance()
                    .getCurrentAssetOpt()
                    .map(path -> path.relativize(file))
                    .orElse(null);

        } catch (IllegalArgumentException e) {
            LOGGER.warning("Can't create asset file of the " + file);
            LOGGER.warning(e);
            return null;
        }
    }

    /**
     * Get an optional of a relative file to the file from the current asset folder.
     *
     * @param file the file.
     * @return the optional of the relative file.
     */
    @FromAnyThread
    public static @NotNull Optional<Path> getAssetFileOpt(@NotNull Path file) {
        return Optional.ofNullable(getAssetFile(file));
    }

    /**
     * Get the path to the file from the current asset folder.
     *
     * @param file the file.
     * @return the relative path.
     */
    @FromAnyThread
    public static @NotNull Path requireAssetFile(@NotNull Path file) {
        return notNull(getAssetFile(file));
    }

    /**
     * Get the absolute path to the file in the current asset.
     *
     * @param assetFile the file.
     * @return the absolute path to the file.
     */
    @FromAnyThread
    public static @Nullable Path getRealFile(@NotNull Path assetFile) {

        var editorConfig = EditorConfig.getInstance();
        var currentAsset = editorConfig.getCurrentAsset();

        if (currentAsset == null) {
            return null;
        }

        return currentAsset.resolve(assetFile);
    }

    /**
     * Get the absolute file by the asset key.
     *
     * @param assetKey the asset key.
     * @return the absolute file.
     */
    @FromAnyThread
    public static @NotNull Path requireRealFile(@NotNull AssetKey<?> assetKey) {
        return requireRealFile(assetKey.getName());
    }

    /**
     * Get the absolute file by the relative asset path.
     *
     * @param assetPath the relative path to the file.
     * @return the absolute file.
     */
    @FromAnyThread
    public static @NotNull Path requireRealFile(@NotNull String assetPath) {
        return EditorConfig.getInstance()
                .getCurrentAssetOpt()
                .filter(path -> StringUtils.isNotEmpty(assetPath))
                .map(path -> path.resolve(assetPath))
                .orElseThrow(() -> new IllegalStateException("Can't build a real file for the asset path " + assetPath));
    }

    /**
     * Get the absolute file by the relative asset path.
     *
     * @param assetPath the relative path to the file.
     * @return the absolute file.
     */
    @FromAnyThread
    public static @Nullable Path getRealFile(@NotNull String assetPath) {

        var editorConfig = EditorConfig.getInstance();
        var currentAsset = editorConfig.getCurrentAsset();

        if (currentAsset == null || StringUtils.isEmpty(assetPath)) {
            return null;
        }

        return currentAsset.resolve(assetPath);
    }

    /**
     * To asset path string.
     *
     * @param path the path
     * @return the valid asset path for the file.
     */
    @FromAnyThread
    public static @NotNull String toAssetPath(@NotNull Path path) {

        if (File.separatorChar == '/') {
            return path.toString();
        }

        return path.toString().replace("\\", "/");
    }

    /**
     * Handle exception.
     *
     * @param logger the logger.
     * @param owner  the owner.
     * @param e      the exception.
     */
    @FromAnyThread
    public static void handleException(@Nullable Logger logger, @Nullable Object owner, @NotNull Throwable e) {
        handleException(logger, owner, e, null);
    }

    /**
     * Handle exception.
     *
     * @param logger   the logger.
     * @param owner    the owner.
     * @param e        the exception.
     * @param callback the callback.
     */
    @FromAnyThread
    public static void handleException(
            @Nullable Logger logger,
            @Nullable Object owner,
            @NotNull Throwable e,
            @Nullable Runnable callback
    ) {

        if (logger == null) {
            logger = LOGGER;
        }

        if (owner == null) {
            logger.warning(e);
        } else {
            logger.warning(owner, e);
        }

        var executorManager = ExecutorManager.getInstance();
        executorManager.addFxTask(() -> {

            GAnalytics.sendException(e, false);

            var localizedMessage = e.getLocalizedMessage();
            var stackTrace = buildStackTrace(e);

            var alert = UiUtils.createErrorAlert(e, localizedMessage, stackTrace);
            alert.show();
            alert.setWidth(500);
            alert.setHeight(220);

            if (callback != null) {
                alert.setOnHidden(event -> callback.run());
            }
        });
    }

    /**
     * Build the stack trace of the exception.
     *
     * @param exception the exception.
     * @return the built stack trace.
     */
    @FromAnyThread
    public static String buildStackTrace(@NotNull Throwable exception) {

        var writer = new StringWriter();
        var printWriter = new PrintWriter(writer);

        exception.printStackTrace(printWriter);

        var stackTrace = writer.toString();

        var level = 0;

        for (var cause = exception.getCause(); cause != null && level < 6; cause = cause.getCause(), level++) {

            writer = new StringWriter();
            printWriter = new PrintWriter(writer);

            cause.printStackTrace(printWriter);

            stackTrace += "\n caused by " + writer.toString();
        }

        return stackTrace;
    }

    /**
     * Open the file in an external editor.
     *
     * @param path the path
     */
    @FromAnyThread
    public static void openFileInExternalEditor(@NotNull Path path) {

        var platform = JmeSystem.getPlatform();
        var commands = new ArrayList<String>();

        if (platform == Platform.MacOSX64 || platform == Platform.MacOSX_PPC64) {
            commands.add("open");
        } else if (platform == Platform.Windows32 || platform == Platform.Windows64) {
            commands.add("cmd");
            commands.add("/c");
            commands.add("start");
        } else if (platform == Platform.Linux32|| platform == Platform.Linux64) {
            commands.add("xdg-open");
        }

        if (commands.isEmpty()) {
            return;
        }

        String url;
        try {
            url = path.toUri().toURL().toString();
        } catch (MalformedURLException e) {
            handleException(LOGGER, null, e);
            return;
        }

        commands.add(url);

        var processBuilder = new ProcessBuilder();
        processBuilder.command(commands);

        try {
            processBuilder.start();
        } catch (IOException e) {
            handleException(LOGGER, null, e);
        }
    }

    /**
     * Open the file in a system explorer.
     *
     * @param path the path
     */
    @FromAnyThread
    public static void openFileInSystemExplorer(@NotNull Path path) {

        var platform = JmeSystem.getPlatform();
        var commands = new ArrayList<String>();

        if (platform == Platform.MacOSX64 || platform == Platform.MacOSX_PPC64) {
            commands.add("open");
            commands.add("-R");
        } else if (platform == Platform.Windows32 || platform == Platform.Windows64) {
            commands.add("explorer");
            commands.add("/select,");
        } else if (platform == Platform.Linux32 || platform == Platform.Linux64) {
            if (isAppExists("nautilus -v")) {
                commands.add("nautilus");
            } else if (isAppExists("dolphin -v")) {
                commands.add("dolphin");
                commands.add("--select");
            } else {
                commands.add("xdg-open");
                if (!Files.isDirectory(path)) {
                    path = path.getParent();
                }
            }
        }

        if (commands.isEmpty()) {
            return;
        }

        String url;
        try {
            url = path.toUri().toURL().toString();
        } catch (MalformedURLException e) {
            handleException(LOGGER, null, e);
            return;
        }

        commands.add(url);

        var processBuilder = new ProcessBuilder();
        processBuilder.command(commands);
        try {
            processBuilder.start();
        } catch (IOException e) {
            handleException(LOGGER, null, e);
        }
    }

    @FromAnyThread
    private static boolean isAppExists(@NotNull String command) {

        var runtime = Runtime.getRuntime();
        int result;
        try {
            var exec = runtime.exec(command);
            result = exec.waitFor();
        } catch (InterruptedException | IOException e) {
            return false;
        }

        return result >= 0;
    }

    /**
     * Convert the object to byte array.
     *
     * @param object the object
     * @return the byte array.
     */
    @FromAnyThread
    public static @NotNull byte[] serialize(@NotNull Serializable object) {

        var bout = new ByteArrayOutputStream();

        try (var out = new ObjectOutputStream(bout)) {
            out.writeObject(object);
        } catch (IOException e) {
            LOGGER.warning(e);
        }

        return bout.toByteArray();
    }

    /**
     * Convert the byte array to object.
     *
     * @param <T>   the type parameter
     * @param bytes the byte array.
     * @return the result object.
     */
    @FromAnyThread
    public static <T> @NotNull T deserialize(@NotNull byte[] bytes) {

        var bin = new ByteArrayInputStream(bytes);

        try (var in = new ExtObjectInputStream(bin)) {
            return unsafeCast(in.readObject());
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get an array of available enum values by an enum value.
     *
     * @param <E>   the type parameter
     * @param value the enum value.
     * @return the array of enum values.
     */
    @FromAnyThread
    public static <E extends Enum<?>> @NotNull E[] getAvailableValues(@NotNull E value) {

        var valueClass = value.getClass();

        if (!valueClass.isEnum()) {
            throw new RuntimeException("The class " + valueClass + " isn't enum.");
        }

        var enumConstants = valueClass.getEnumConstants();

        return unsafeCast(enumConstants);
    }

    /**
     * Try to create an user object using asset classpath and additional classpath.
     *
     * @param <T>        the type parameter
     * @param owner      the requester.
     * @param className  the classname.
     * @param resultType the result type.
     * @return the new instance or null.
     */
    @FromAnyThread
    public static <T> @Nullable T tryToCreateUserObject(
            @NotNull Object owner,
            @NotNull String className,
            @NotNull Class<T> resultType
    ) {

        var resourceManager = ResourceManager.getInstance();
        var classpathManager = ClasspathManager.getInstance();

        Object newExample = null;
        try {
            newExample = ClassUtils.newInstance(className);
        } catch (RuntimeException e) {

            var classLoaders = resourceManager.getClassLoaders();

            for (var classLoader : classLoaders) {
                try {
                    var targetClass = classLoader.loadClass(className);
                    newExample = ClassUtils.newInstance(targetClass);
                } catch (ClassNotFoundException ex) {
                    LOGGER.warning(owner, e);
                }
            }

            var librariesLoader = classpathManager.getLibrariesLoader();

            if (librariesLoader != null) {
                try {
                    var targetClass = librariesLoader.loadClass(className);
                    newExample = ClassUtils.newInstance(targetClass);
                } catch (final ClassNotFoundException ex) {
                    LOGGER.warning(owner, e);
                }
            }
        }

        return cast(resultType, newExample);
    }

    /**
     * Get a default layer of the change consumer.
     *
     * @param consumer the change consumer.
     * @return the default layer or null.
     */
    @FromAnyThread
    public static @Nullable SceneLayer getDefaultLayer(@NotNull ChangeConsumer consumer) {

        if (!(consumer instanceof SceneChangeConsumer)) {
            return null;
        }

        var sceneNode = ((SceneChangeConsumer) consumer).getCurrentModel();
        var layers = sceneNode.getLayers();

        if (layers.isEmpty()) {
            return null;
        }

        return layers.get(0);
    }

    /**
     * Return true if the asset key is null or empty.
     *
     * @param assetKey the asset key.
     * @return true if the asset key is null or empty.
     */
    public static boolean isEmpty(@Nullable AssetKey<?> assetKey) {
        return assetKey == null || StringUtils.isEmpty(assetKey.getName());
    }

    /**
     * Return the another string if the key is empty.
     *
     * @param assetKey the asset key.
     * @return the another string if the key is empty.
     */
    public static String ifEmpty(@Nullable AssetKey<?> assetKey, @NotNull String another) {
        return isEmpty(assetKey) ? another : assetKey.getName();
    }

    /**
     * Open the asset resource in an editor.
     *
     * @param assetKey the asset key.
     */
    @FromAnyThread
    public static void openInEditor(@Nullable AssetKey<?> assetKey) {

        if (assetKey == null || StringUtils.isEmpty(assetKey.getName())) {
            LOGGER.warning("The asset key " + assetKey + " is empty or null.");
            return;
        }

        var assetPath = assetKey.getName();
        var realFile = requireRealFile(assetPath);

        if (!Files.exists(realFile)) {
            LOGGER.warning("The file " + realFile + " is not exists.");
            return;
        }

        FxEventManager.getInstance()
                .notify(new RequestedOpenFileEvent(realFile));
    }

    /**
     * Get the list of files in the current dragboard.
     *
     * @param dragboard the current dragboard.
     * @return the list of files.
     */
    @FromAnyThread
    public static @NotNull List<File> getFiles(@NotNull Dragboard dragboard) {
        List<File> files = unsafeCast(dragboard.getContent(DataFormat.FILES));
        return files == null ? Collections.emptyList() : files;
    }

    /**
     * Get an array of enum constants by the class.
     *
     * @param enumType the enum's class.
     * @param <T>      the enum's type.
     * @return the array of enum's constants.
     */
    @FromAnyThread
    public static <T> T[] getEnumValues(@NotNull Class<?> enumType) {

        if(!enumType.isEnum()) {
            throw new IllegalArgumentException("The type " + enumType + " isn't a enum.");
        }

        return unsafeCast(ENUM_VALUES_LOCAL.get()
                .get(enumType, type -> (Enum<?>[]) type.getEnumConstants()));
    }

    /**
     * Find a root key of the spatial.
     *
     * @param spatial the spatial.
     * @return the root key or null.
     */
    public static @Nullable String findRootKey(@Nullable Spatial spatial) {

        if(spatial == null) {
            return null;
        }

        return NodeUtils.<Spatial>findParentOpt(spatial, sp -> sp.getKey() != null)
                .map(Spatial::getKey)
                .map(AssetKey::getName)
                .orElse(null);
    }

    /**
     * Lock the render phase in jME.
     *
     * @return the lock stamp.
     */
    @FromAnyThread
    public static long renderLock() {
        return jmeApplication.asyncLock();
    }

    /**
     * Unlock the render phase in jME.
     *
     * @param stamp the lock stamp.
     */
    @FromAnyThread
    public static void renderUnlock(long stamp) {
        jmeApplication.asyncUnlock(stamp);
    }

    /**
     * Build an asset key for the real file.
     *
     * @param realFile the real file.
     * @param constructor the asset key's constructor.
     * @param <T> the asset key's type.
     * @return the asset key.
     */
    public static <T extends AssetKey<?>> @NotNull T realFileToKey(
            @NotNull Path realFile,
            @NotNull Function<String, T> constructor
    ) {
        return getAssetFileOpt(realFile)
                .map(EditorUtils::toAssetPath)
                .map(constructor)
                .orElseThrow(() -> new RuntimeException("Can't build an asset key for the file " + realFile));
    }

    /**
     * Clear the asset cache.
     */
    @JmeThread
    public static void clearAssetCache() {
        getAssetManager().clearCache();
    }
}
