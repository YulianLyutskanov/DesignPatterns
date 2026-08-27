package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;

import java.nio.file.Path;

public interface FileSystemEntity {
    Path getAbsolutePath(); // Getter for relative path

    long getSize(); // Getter for size

    void accept(FileSystemEntityVisitor visitor); // Accept method for the visitor pattern
}
