package com.ss.editor.remote.control.client;

import com.ss.editor.annotation.BackgroundThread;
import com.ss.editor.manager.ClasspathManager;
import com.ss.rlib.common.network.ConnectionOwner;
import com.ss.rlib.common.network.annotation.PacketDescription;
import com.ss.rlib.common.util.StringUtils;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The command to init local classpath in jMB.
 *
 * @author JavaSaBr
 */
@PacketDescription(id = 4)
public class InitLocalClasspathClientCommand extends ClientCommand {

    @Override
    @BackgroundThread
    protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {

        var libraries = ArrayFactory.<Path>newArray(Path.class);

        for (int i = 0, length = readInt(buffer); i < length; i++) {
            libraries.add(Paths.get(readString(buffer)));
        }

        var outputPath = readString(buffer);
        var output = StringUtils.isEmpty(outputPath) ? null : Paths.get(outputPath);

        ClasspathManager.getInstance()
                .loadLocalLibraries(libraries)
                .loadLocalClasses(output);
    }
}
