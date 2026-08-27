package com.fmi.dp.visitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import com.fmi.dp.observer.ObserverApi;
import com.fmi.dp.visitor.formateditors.FormatEditor;
import com.fmi.dp.visitor.memento.ProcessedFilesSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;

public class HashStreamWriterVisitorTest {
    private HashStreamWriterVisitor visitor;
    private OutputStream outputStream;
    private ChecksumCalculator checksumCalculator;
    private FormatEditor formatEditor;
    private SingleFile singleFile;
    private Directory directory;
    private Shortcut shortcut;
    private ObserverApi observer;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        checksumCalculator = mock(ChecksumCalculator.class);
        formatEditor = mock(FormatEditor.class);
        visitor = new HashStreamWriterVisitor(outputStream, checksumCalculator, formatEditor);

        singleFile = mock(SingleFile.class);
        directory = mock(Directory.class);
        shortcut = mock(Shortcut.class);
        observer = mock(ObserverApi.class);
    }

    @Test
    void testVisitSingleFile() throws IOException {
        Path temp = Files.createTempFile("temp", ".txt");
        when(singleFile.getAbsolutePath()).thenReturn(temp);
        when(singleFile.getSize()).thenReturn(1024L);
        when(checksumCalculator.calculate(any())).thenReturn("mockChecksum");

        visitor.visit(singleFile);

        verify(checksumCalculator).calculate(any());
        verify(formatEditor).writeChecksum(any(), eq(singleFile), eq("mockChecksum"));
    }

    @Test
    void testVisitDirectory() throws IOException {
        when(directory.getAbsolutePath()).thenReturn(Path.of("/test/folder"));
        when(directory.getSize()).thenReturn(2048L);
        when(directory.getChildren()).thenReturn(Collections.singleton(singleFile));

        visitor.visit(directory);

        verify(singleFile).accept(visitor);
        verify(formatEditor).writeChecksumAtBeginning(any(), any());
        verify(formatEditor).writeChecksumAtEnd(any(), any());
    }

    @Test
    void testVisitShortcut() throws IOException {
        when(shortcut.getAbsolutePath()).thenReturn(Path.of("/test/shortcut"));
        when(shortcut.getSize()).thenReturn(512L);
        when(shortcut.getTarget()).thenReturn(singleFile);
        Path path = Files.createTempFile("some", ".txt");
        when(shortcut.getAbsolutePath()).thenReturn(path);
        when(checksumCalculator.calculate(any())).thenReturn("mockChecksum");
        doNothing().when(formatEditor).writeChecksum(any(), any(), any());

        visitor.visit(shortcut);

        verify(singleFile).accept(visitor);
    }

    @Test
    void testAttachObserver() {
        visitor.attach(observer);
        verify(checksumCalculator).attach(observer);
    }

    @Test
    void testPauseAndResume() {
        visitor.pause();
        assertTrue(visitor.isStopped());

        ProcessedFilesSnapshot snapshot = new ProcessedFilesSnapshot(new HashSet<>(), directory);
        visitor.restore(snapshot);
        assertFalse(visitor.isStopped());
    }

    @Test
    void testIOExceptionDuringFileProcessing() throws IOException {
        when(singleFile.getAbsolutePath()).thenReturn(Path.of("/test/file.txt"));
        when(singleFile.getSize()).thenReturn(1024L);
        when(checksumCalculator.calculate(any())).thenReturn("ok");
        doThrow(new IOException("IO Error")).when(formatEditor).writeChecksum(any(), any(), any());
        assertThrows(UncheckedIOException.class, () -> visitor.visit(singleFile));
    }
}
