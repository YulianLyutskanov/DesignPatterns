package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Directory extends BaseFileSystemEntity {
    @NotNull
    private final Collection<@NotNull FileSystemEntity> children;

    public Directory(@NotNull Path relativePath, long size, @NotNull Collection<@NotNull FileSystemEntity> children) {
        super(relativePath, size);
        this.children = children;
    }

    public Directory(@NotNull Path relativePath) {
        super(relativePath, 0);
        this.children = new ArrayList<>();
    }

    public Collection<FileSystemEntity> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    public void addChild(@NotNull FileSystemEntity child) {
        setSize(this.getSize() + child.getSize()); // Update the size of the directory
        children.add(child); // Add the child to the collection
    }

    @Override
    public void accept(@NotNull FileSystemEntityVisitor visitor) {
        visitor.visit(this); // Call the visitor's visit method for this directory
    }
}
