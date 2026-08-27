package com.fmi.dp.reporter;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.writer.BaseStreamWriter;
import com.fmi.dp.observer.FileMessage;
import com.fmi.dp.observer.ObservableApi;
import com.fmi.dp.observer.ObserverApi;
import com.fmi.dp.reporter.memento.ProgressReporterMementoApi;
import com.fmi.dp.reporter.memento.ProgressSnapshot;
import com.fmi.dp.visitor.HashStreamWriterVisitor;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ProgressReporter extends BaseStreamWriter implements ObserverApi, ProgressReporterMementoApi {
    private static final int MAX_PERCENTAGE = 100;

    @NotNull
    private final Stopwatch stopwatch = new Stopwatch();
    private long expectedBytesToRead = 0;
    private long readBytes = 0L;

    public ProgressReporter(@NotNull OutputStream outputStream) {
        super(outputStream);
    }

    public boolean isStopped() {
        return !stopwatch.isRunning();
    }

    public long getElapsedMilliseconds() {
        return stopwatch.elapsedMilliseconds();
    }

    public ProgressSnapshot getSnapshot() {
        return new ProgressSnapshot(readBytes, expectedBytesToRead, getElapsedMilliseconds());
    }

    public void pause() {
        stopwatch.stop();
    }

    public void restore(@NotNull ProgressSnapshot snapshot) {
        expectedBytesToRead = snapshot.expectedBytesToRead();
        readBytes = snapshot.readBytes();
        stopwatch.reset();
        stopwatch.start();
    }

    public void startTimer(long expectedTotalBytes) {
        expectedBytesToRead = expectedTotalBytes;
        readBytes = 0L;
        stopwatch.reset();
        stopwatch.start();
    }

    public long endTimer() {
        stopwatch.stop();
        return getElapsedMilliseconds();
    }

    @Override
    public void update(@NotNull ObservableApi sender, @NotNull FileMessage message) {
        try {
            if (message.alreadyProcessed()) {
                streamWriter.write("File: " + message.fileName() +
                        " - with length " + message.size() + " has already been proceeded!"
                        + System.lineSeparator());
                streamWriter.flush();
                return;
            }
            if (sender instanceof ChecksumCalculator) {
                readBytes += message.size();
                streamWriter.write("Total processed " + readBytes + "b");
            } else if (sender instanceof HashStreamWriterVisitor) {
                streamWriter.write(System.lineSeparator() + "Processing " + message.fileName() + " - with length "
                        + message.size() + "b" +  System.lineSeparator());
            } else {
                throw new IllegalArgumentException("Invalid sender type");
            }
            streamWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while writing in: " + streamWriter +
                    " from ProgressReporter", e);
        }
        printPercentage();
    }

    private void printPercentage() {
        double percentage = (readBytes / (double) expectedBytesToRead) * MAX_PERCENTAGE;
        long remainingTime = (long) ((stopwatch.elapsedMilliseconds() / percentage) * (MAX_PERCENTAGE - percentage));
        try {
            streamWriter.write(" Total processed " + String.format("%.6f", percentage) +
                    "%, remaining time " + remainingTime + "ms" + System.lineSeparator());
            streamWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Error while writing in: " + streamWriter +
                   " percentage in streamWriter", e);
        }
    }
}
