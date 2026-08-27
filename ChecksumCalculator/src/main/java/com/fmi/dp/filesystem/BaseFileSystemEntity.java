package com.fmi.dp.filesystem;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public abstract class BaseFileSystemEntity implements FileSystemEntity {
    @NotNull
    private final Path absolutePath;
    private long size;

    public BaseFileSystemEntity(@NotNull Path path, long size) {
        this.absolutePath = path.toAbsolutePath();
        this.size = size;
    }

    @Override
    @NotNull
    public Path getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public long getSize() {
        return size;
    }

    protected void setSize(long size) {
        this.size = size;
    }
}
