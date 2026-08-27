package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Shortcut extends BaseFileSystemEntity {
    private FileSystemEntity target;

    public Shortcut(@NotNull Path relativePath, long size, @NotNull FileSystemEntity target) {
        super(relativePath, size);
        this.target = target;
    }

    public Shortcut( Path relativePath, long size) {
        super(relativePath, size);
    }

    public FileSystemEntity getTarget() {
        return target;
    }

    public void setTarget(FileSystemEntity target) {
        this.target = target;
    }

    @Override
    public long getSize() {
        return super.getSize() + (target == null ? 0 : target.getSize());
    }

    // Bad design, but this kind of entity should not be visited
    @Override
    public void accept( FileSystemEntityVisitor visitor) {
        visitor.visit(this);
    }
}
