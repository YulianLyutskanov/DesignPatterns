package com.fmi.dp.filesystembuilder;

import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.FileSystemEntity;
import com.fmi.dp.filesystem.SingleFile;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileSystemBuilderTest {
    private final FileSystemBuilderApi builder = new FileSystemBuilder();

    @Test
    void testBuildFileWithSameTargetAsPath() {
        assertThrows(IllegalArgumentException.class, () -> builder.build(Path.of("same"), Path.of("same")));
    }

    @Test
    void testBuildSingleFile() throws IOException {
        Path tempFile = Files.createTempFile("testFile", ".txt");
        assertEquals(SingleFile.class, builder.build(tempFile, Path.of("different")).getClass());
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testBuildDirectory() throws IOException {
        Path customDir =  Files.createTempDirectory("dirPath");
        Path tempFile = Files.createTempFile(customDir, "customTemp", ".txt");
        var result = builder.build(customDir, Path.of("different"));
        assertEquals(Directory.class, result.getClass());

        Directory dir = (Directory) result;
        assertTrue(dir.getAbsolutePath().toString().contains("dirPath"));
        for (var child : dir.getChildren()) {
            assertTrue(child.getAbsolutePath().toString().contains("customTemp"));
        }
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testBuildSymlinkWithoutCreatingFiles() {
        Path symlink = Path.of("symlinkTest");
        Path target = Path.of("testFile.txt");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(symlink)).thenReturn(true);
            mockedFiles.when(() -> Files.exists(target)).thenReturn(true);
            mockedFiles.when(() -> Files.isRegularFile(target)).thenReturn(true);
            mockedFiles.when(() -> Files.isSymbolicLink(symlink)).thenReturn(true);
            mockedFiles.when(() -> Files.readSymbolicLink(symlink)).thenReturn(target);

            // Execute test logic
            FileSystemEntity result = builder.build(symlink, Path.of("different"));

            // Validate that it correctly identifies a symbolic link
            assertEquals(SingleFile.class, result.getClass());
            assertTrue(result.getAbsolutePath().toString().contains("testFile.txt"));
        }
    }
}
