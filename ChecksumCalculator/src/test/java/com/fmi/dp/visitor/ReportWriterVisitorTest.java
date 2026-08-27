package com.fmi.dp.visitor;

import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportWriterVisitorTest {
    @Test
    void testValidSingleFileVisit() throws IOException {
        SingleFile singleFile = mock(SingleFile.class);
        when(singleFile.getAbsolutePath()).thenReturn(Path.of("somePath"));
        when(singleFile.getSize()).thenReturn(21L);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ReportWriterVisitor visitor = new ReportWriterVisitor(outputStream);
        visitor.visit(singleFile);

        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(
                        new ByteArrayInputStream(outputStream.toByteArray()))));

        assertEquals(String.format("We will file visit: %s - %db", singleFile.getAbsolutePath(), singleFile.getSize()),
                inputReader.readLine());
    }

    @Test
    void testValidDirectoryVisit() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ReportWriterVisitor visitor = new ReportWriterVisitor(outputStream);

        Directory directory = mock(Directory.class);
        when(directory.getAbsolutePath()).thenReturn(Path.of("dirPath"));
        when(directory.getSize()).thenReturn(71L);

        SingleFile singleFile1 = mock(SingleFile.class);
        when(singleFile1.getAbsolutePath()).thenReturn(Path.of("somePath1"));
        when(singleFile1.getSize()).thenReturn(21L);
        doAnswer(_ -> {
            visitor.visit(singleFile1);
            return null;
        }).when(singleFile1).accept(visitor);

        SingleFile singleFile2 = mock(SingleFile.class);
        when(singleFile2.getAbsolutePath()).thenReturn(Path.of("somePath2"));
        when(singleFile2.getSize()).thenReturn(50L);
        doAnswer(_ -> {
            visitor.visit(singleFile2);
            return null;
        }).when(singleFile2).accept(visitor);

        when(directory.getChildren()).thenReturn(List.of(singleFile1, singleFile2));

        visitor.visit(directory);
        outputStream.flush();

        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(
                        new ByteArrayInputStream(outputStream.toByteArray()))));

        assertEquals(String.format("We will file visit: %s - %db", directory.getAbsolutePath(), directory.getSize()),
                inputReader.readLine());
        assertEquals(String.format("We will file visit: %s - %db", singleFile1.getAbsolutePath(), singleFile1.getSize()),
                inputReader.readLine());
        assertEquals(String.format("We will file visit: %s - %db", singleFile2.getAbsolutePath(), singleFile2.getSize()),
                inputReader.readLine());
    }

    @Test
    void testValidShortcutVisit() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ReportWriterVisitor visitor = new ReportWriterVisitor(outputStream);

        Shortcut shortcut = mock(Shortcut.class);
        when(shortcut.getAbsolutePath()).thenReturn(Path.of("shPath"));
        when(shortcut.getSize()).thenReturn(71L);

        SingleFile singleFile1 = mock(SingleFile.class);
        when(singleFile1.getAbsolutePath()).thenReturn(Path.of("somePath1"));
        when(singleFile1.getSize()).thenReturn(21L);
        doAnswer(_ -> {
            visitor.visit(singleFile1);
            return null;
        }).when(singleFile1).accept(visitor);

        when(shortcut.getTarget()).thenReturn(singleFile1);

        visitor.visit(shortcut);
        outputStream.flush();

        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(
                        new ByteArrayInputStream(outputStream.toByteArray()))));

        assertEquals(String.format("We will file visit: %s - %db", shortcut.getAbsolutePath(), shortcut.getSize()),
                inputReader.readLine());
        assertEquals(String.format("We will file visit: %s - %db", singleFile1.getAbsolutePath(), singleFile1.getSize()),
                inputReader.readLine());
    }
}
