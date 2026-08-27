package com.fmi.dp.visitor.memento;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.jetbrains.annotations.NotNull;

public interface FileSystemEntityMementoVisitor extends FileSystemEntityVisitor {
    boolean hasFinished();

    boolean isStopped();

    void pause();

    ProcessedFilesSnapshot getSnapshot();

    void restore(@NotNull ProcessedFilesSnapshot snapshot);
}
