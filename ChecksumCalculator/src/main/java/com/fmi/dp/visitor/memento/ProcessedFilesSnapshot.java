package com.fmi.dp.visitor.memento;

import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public record ProcessedFilesSnapshot(@NotNull Set<@NotNull Path> visited, @NotNull FileSystemEntity root) {

    public ProcessedFilesSnapshot(@NotNull Set<@NotNull Path> visited, @NotNull FileSystemEntity root) {
        this.visited = new HashSet<>(visited); // Using HashSet for immutable-like behavior
        this.root = root;
    }
}
