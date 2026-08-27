package com.fmi.dp.filesystembuilder;

import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface FileSystemBuilderApi {
    FileSystemEntity build(@NotNull Path path, @NotNull Path checksumTargetPath);

}
