package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class SingleFile extends BaseFileSystemEntity {

    public SingleFile(@NotNull Path relativePath, long size) {
        super(relativePath, size);
    }

    @Override
    public void accept(@NotNull FileSystemEntityVisitor visitor) {
        visitor.visit(this); // Call the visitor's visit method for this file
    }
}
