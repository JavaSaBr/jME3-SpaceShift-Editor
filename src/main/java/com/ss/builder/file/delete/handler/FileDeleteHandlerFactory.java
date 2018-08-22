package com.ss.builder.file.delete.handler;

import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.file.delete.handler.impl.DeleteMaterialsModelFileDeleteHandler;
import com.ss.builder.annotation.FromAnyThread;
import com.ss.builder.file.delete.handler.impl.DeleteMaterialsModelFileDeleteHandler;
import org.jetbrains.annotations.NotNull;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;

import java.nio.file.Path;

/**
 * The factory of delete handlers of deleted file.
 *
 * @author JavaSaBr
 */
public class FileDeleteHandlerFactory {

    @NotNull
    private static final Array<FileDeleteHandler> HANDLERS = ArrayFactory.newArray(FileDeleteHandler.class);

    static {
        HANDLERS.add(new DeleteMaterialsModelFileDeleteHandler());
    }

    /**
     * Find handlers for the file.
     *
     * @param file the file.
     * @return the list of handlers.
     */
    @FromAnyThread
    public static @NotNull Array<FileDeleteHandler> findFor(@NotNull final Path file) {

        final Array<FileDeleteHandler> result = ArrayFactory.newArray(FileDeleteHandler.class);

        HANDLERS.forEach(result, file, (handler, toCollect, f) -> handler.isNeedHandle(f),
                (handler, toCollect, f) -> toCollect.add(handler.clone()));

        return result;
    }
}
