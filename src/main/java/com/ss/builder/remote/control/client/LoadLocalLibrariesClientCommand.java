package com.ss.builder.remote.control.client;

import com.ss.builder.annotation.BackgroundThread;
import com.ss.builder.manager.ClasspathManager;
import com.ss.rlib.common.network.ConnectionOwner;
import com.ss.rlib.common.network.annotation.PacketDescription;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The command to load local libraries in jMB.
 *
 * @author JavaSaBr
 */
@PacketDescription(id = 2)
public class LoadLocalLibrariesClientCommand extends ClientCommand {

    @Override
    @BackgroundThread
    protected void readImpl(@NotNull ConnectionOwner owner, @NotNull ByteBuffer buffer) {

        var libraries = Array.ofType(Path.class);

        for (int i = 0, length = readInt(buffer); i < length; i++) {
            libraries.add(Paths.get(readString(buffer)));
        }

        ClasspathManager.getInstance()
                .loadLocalLibraries(libraries);
    }
}
