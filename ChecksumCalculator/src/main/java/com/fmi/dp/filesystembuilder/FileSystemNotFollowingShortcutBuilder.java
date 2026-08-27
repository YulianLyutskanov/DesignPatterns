package com.fmi.dp.filesystembuilder;

import com.fmi.dp.exceptions.PathWithNoInformationForAnalyze;
import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.FileSystemEntity;
import com.fmi.dp.filesystem.SingleFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileSystemNotFollowingShortcutBuilder implements FileSystemBuilderApi {

    //when shortcut => doesn't go through
    //when symlink => doesn't go through
    @Override
    public FileSystemEntity build(@NotNull Path path, @NotNull Path checksumTargetPath) {
        if (path.equals(checksumTargetPath)) {
            throw new IllegalArgumentException("Target checksum file path and path of the file to be approached " +
                    path + " cannot match!");
        }
        var result = buildRec(path);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new PathWithNoInformationForAnalyze("The current path: " + path +
                    " has symbol link which is not analyzable in this mode.");
        }
    }

    private Optional<FileSystemEntity> buildRec(@NotNull Path path) {
        //return because symlink does not contain anything unlike shortcut
        if (Files.isSymbolicLink(path)) {
            return Optional.empty();
        }

        //If it is non loop file (.zip, .txt, .xml and so on) or shortcut it will go here
        if (Files.isRegularFile(path)) {
            return Optional.of(new SingleFile(path, path.toFile().length()));
        }

        if (Files.isDirectory(path)) {
            Directory directory = new Directory(path);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    var child = buildRec(entry);
                    child.ifPresent(directory::addChild);
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Error occurred while adding children to directory: " +
                        directory.getAbsolutePath(), e);
            }
            return Optional.of(directory);
        }

        throw new IllegalArgumentException("Invalid path: " + path);
    }
}
