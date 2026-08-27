package com.fmi.dp.visitor;

import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import org.jetbrains.annotations.NotNull;

public interface FileSystemEntityVisitor {
    void visit(@NotNull SingleFile file);

    void visit(@NotNull Directory directory);

    void visit(@NotNull Shortcut file);
}
