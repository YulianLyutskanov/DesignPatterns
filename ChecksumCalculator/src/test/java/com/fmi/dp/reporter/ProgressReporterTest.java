package com.fmi.dp.reporter;

import com.fmi.dp.observer.FileMessage;
import com.fmi.dp.observer.ObservableApi;
import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.observer.ObserverApi;
import com.fmi.dp.reporter.memento.ProgressSnapshot;
import com.fmi.dp.visitor.HashStreamWriterVisitor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressReporterTest {

    private static ProgressReporter progressReporter;
    private static ByteArrayOutputStream outputStream;

    private ObservableApi mockObservable = mock(ChecksumCalculator.class);
    private ObservableApi mockObservable2 = mock(HashStreamWriterVisitor.class);

    @BeforeAll
    static void setUp() {
        outputStream = new ByteArrayOutputStream();
        progressReporter = new ProgressReporter(outputStream);
    }

    @Test
    void testStartTimer() {
        progressReporter.startTimer(1000L);

        assertFalse(progressReporter.isStopped());
        assertEquals(0L, progressReporter.getElapsedMilliseconds());
    }

    @Test
    void testPauseTimer() {
        progressReporter.startTimer(1000L);
        progressReporter.pause();

        assertTrue(progressReporter.isStopped());
    }

    @Test
    void testRestoreTimer() {
        progressReporter.startTimer(1000L);
        progressReporter.pause();

        ProgressSnapshot snapshot = progressReporter.getSnapshot();
        progressReporter.restore(snapshot);

        assertFalse(progressReporter.isStopped());
    }

    @Test
    void testEndTimer() {
        progressReporter.startTimer(1000L);
        long elapsedTime = progressReporter.endTimer();

        assertTrue(elapsedTime >= 0);
    }

    @Test
    void testChecksumCalculator() throws IOException {
        ByteArrayOutputStream bufferedCriteria = new ByteArrayOutputStream();
        ProgressReporter reporter = new ProgressReporter(bufferedCriteria);
        reporter.startTimer(1000L);
        FileMessage message = new FileMessage("file", 20, false);
        reporter.update(mock(ChecksumCalculator.class), message);
        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(new ByteArrayInputStream(bufferedCriteria.toByteArray()))));
        assertTrue(inputReader.readLine().contains("Total processed " + message.size() + "b" + " Total processed " +
                String.format("%.6f", 20 / (double)1000 * 100)));
    }

    @Test
    void testHashWriter() throws IOException {
        ByteArrayOutputStream bufferedCriteria = new ByteArrayOutputStream();
        ProgressReporter reporter = new ProgressReporter(bufferedCriteria);
        reporter.startTimer(1000L);
        FileMessage message = new FileMessage("file", 20, false);
        reporter.update(mock(HashStreamWriterVisitor.class), message);
        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(new ByteArrayInputStream(bufferedCriteria.toByteArray()))));
        inputReader.readLine();
        assertEquals("Processing " + message.fileName() + " - with length "
                + message.size() + "b", inputReader.readLine());
        assertTrue(inputReader.readLine().contains(" Total processed " +
                String.format("%.6f", 0.0) + "%"));
    }

    @Test
    void testAlreadyProcessed() throws IOException {
        ByteArrayOutputStream bufferedCriteria = new ByteArrayOutputStream();
        ProgressReporter reporter = new ProgressReporter(bufferedCriteria);
        FileMessage message = new FileMessage("file", 20, true);
        reporter.update(mock(HashStreamWriterVisitor.class), message);
        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(new ByteArrayInputStream(bufferedCriteria.toByteArray()))));
        assertEquals("File: " + message.fileName() +
                " - with length " + message.size() + " has already been proceeded!", inputReader.readLine());
    }

    @Test
    void testInvalidObservable() throws IOException {
        class Invalid implements ObservableApi {
            @Override
            public void attach(@NotNull ObserverApi observer) {
            }

            @Override
            public void detach(@NotNull ObserverApi observer) {
            }

            @Override
            public void notify(@NotNull ObservableApi sender, @NotNull FileMessage message) {
            }
        }
        ByteArrayOutputStream bufferedCriteria = new ByteArrayOutputStream();
        ProgressReporter reporter = new ProgressReporter(bufferedCriteria);
        FileMessage message = new FileMessage("file", 20, false);
        assertThrows(IllegalArgumentException.class, () -> reporter.update(mock(Invalid.class), message));
    }
}
