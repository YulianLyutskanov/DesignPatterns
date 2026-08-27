package com.fmi.dp.filesystembuilder;

import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.FileSystemEntity;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileSystemBuilder implements FileSystemBuilderApi {
    @Override
    public FileSystemEntity build(@NotNull Path path, @NotNull Path checksumTargetPath) {
        Map<Path, FileSystemEntity> visitedSystemEntitiesCurrentBuild = new HashMap<>();

        //If we see the checksum file we should skip it because it's size is changing
        //during the process, because we cannot predict it's size
        visitedSystemEntitiesCurrentBuild.put(checksumTargetPath, null);

        var result = buildRec(path, visitedSystemEntitiesCurrentBuild);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new IllegalArgumentException("Target checksum file path and path of the file to be approached " +
                    path + " cannot match!");
        }
    }

    //when shortcut => goes through
    //when symlink => goes through
    private Optional<FileSystemEntity> buildRec(@NotNull Path path,
                                                @NotNull Map<Path, FileSystemEntity> visitedSystemEntities) {
        if (visitedSystemEntities.containsKey(path)) {
            return Optional.empty();
        }

        if (Files.isSymbolicLink(path)) {
            try {
                return buildRec(Files.readSymbolicLink(path), visitedSystemEntities);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        Optional<FileSystemEntity> resultIfShortCut = createShortCut(path, visitedSystemEntities);
        if (resultIfShortCut.isPresent()) {
            return resultIfShortCut;
        }

        if (Files.isRegularFile(path)) {
            var file = new SingleFile(path, path.toFile().length());
            visitedSystemEntities.put(path, file);
            return Optional.of(file);
        }
        if (Files.isDirectory(path)) {
            return Optional.of(createDirectory(path, visitedSystemEntities));
        }
        throw new IllegalArgumentException("Invalid path: " + path);
    }

    private Optional<Path> getShortcutTarget(@NotNull Path path) {
        if (!path.getFileName().toString().toLowerCase().endsWith(".lnk")) {
            return Optional.empty();
        }
        try {
            Path targetPath = getTargetPath(path);
            return Optional.of(targetPath);
        } catch (IOException e) {
            System.err.println("Failed to read shortcut: " + e.getMessage());
            return Optional.empty();
        }
    }

    //Not sure how to extract shortcut target path, so using predefined methods
    private Path getTargetPath(@NotNull Path path) throws IOException {
        FileSystemManager fsManager = VFS.getManager();

        try (FileObject shortcut = fsManager.resolveFile(path.toUri().toString())) {
            if (!shortcut.exists()) {
                throw new IOException("File does not exist: " + path);
            }

            // Attempt to resolve symbolic links (for Unix/Linux/macOS)
            if (Files.isSymbolicLink(path)) {
                return path.toRealPath(); // Resolves to actual target
            }

            // Attempt to resolve Windows shortcuts (.lnk files)
            if (path.toString().endsWith(".lnk")) {
                return resolveWindowsShortcut(path);
            }

            // If it's not a symlink or shortcut, return the resolved absolute path
            return Path.of(shortcut.getName().getPath()).toAbsolutePath();
        }
        // Cleanup
    }

    private Path resolveWindowsShortcut(Path shortcutPath) throws IOException {
        try {
            ProcessBuilder pb = new ProcessBuilder("powershell",
                    "-ExecutionPolicy", "Bypass",
                    "-NoProfile",
                    "-Command",
                    "(New-Object -ComObject WScript.Shell).CreateShortcut('" + shortcutPath + "').TargetPath");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String target = new String(process.getInputStream().readAllBytes()).trim();

            if (!target.isEmpty()) {
                return Path.of(target);
            } else {
                throw new IOException("Failed to resolve Windows shortcut: " + shortcutPath);
            }
        } catch (Exception e) {
            throw new IOException("Error resolving Windows shortcut: " + shortcutPath, e);
        }
    }

    private Optional<FileSystemEntity> createShortCut(@NotNull Path path,
                                                      @NotNull Map<Path, FileSystemEntity> visitedSystemEntities) {
        Optional<Path> isShortcut = getShortcutTarget(path);
        if (isShortcut.isPresent()) {
            Shortcut shortcut = new Shortcut(path, path.toFile().length());
            visitedSystemEntities.put(path, shortcut);

            var result = buildRec(isShortcut.get(), visitedSystemEntities);
            result.ifPresent(shortcut::setTarget);
            return Optional.of(shortcut);
        }
        return Optional.empty();
    }

    private FileSystemEntity createDirectory(@NotNull Path path,
                                             @NotNull Map<Path, FileSystemEntity> visitedSystemEntities) {
        Directory directory = new Directory(path);
        visitedSystemEntities.put(path, directory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                var child = buildRec(entry, visitedSystemEntities);
                child.ifPresent(directory::addChild);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while adding children to directory: " +
                    directory.getAbsolutePath(), e);
        }
        return directory;
    }
}
