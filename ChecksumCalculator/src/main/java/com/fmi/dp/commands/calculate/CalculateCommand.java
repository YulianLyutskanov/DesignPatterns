package com.fmi.dp.commands.calculate;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.filesystem.FileSystemEntity;
import com.fmi.dp.filesystembuilder.FileSystemBuilder;
import com.fmi.dp.filesystembuilder.FileSystemNotFollowingShortcutBuilder;
import com.fmi.dp.filesystembuilder.FileSystemBuilderApi;
import com.fmi.dp.reporter.ProgressReporter;
import com.fmi.dp.reporter.memento.ProgressReporterMementoApi;
import com.fmi.dp.reporter.memento.ProgressSnapshot;
import com.fmi.dp.visitor.HashStreamWriterVisitor;
import com.fmi.dp.visitor.ReportWriterVisitor;
import com.fmi.dp.visitor.formateditors.FormatEditor;
import com.fmi.dp.visitor.memento.FileSystemEntityMementoVisitor;
import com.fmi.dp.visitor.memento.ProcessedFilesSnapshot;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

//Facade + Command
public class CalculateCommand implements ModeCommand {
    @NotNull
    private final FileSystemBuilderApi fileSystemBuilder;

    @NotNull
    private final ChecksumCalculator calculator;
    @NotNull
    private final Path from;
    @NotNull
    private final Path checksums;

    private FileSystemEntityMementoVisitor streamVisitor;
    private ProgressReporterMementoApi progressReporter;

    protected ProgressSnapshot progressSnapshot;
    protected ProcessedFilesSnapshot processedFilesSnapshot;

    private OutputStream outputStream;

    public CalculateCommand(@NotNull Path from, @NotNull Path checksums,
                            @NotNull ChecksumCalculator calculator,
                            @NotNull FileSystemBuilderApi fileSystemBuilder) {
        this.calculator = calculator;
        this.from = from;
        this.checksums = checksums;
        this.fileSystemBuilder = fileSystemBuilder;
    }

    public CalculateCommand(@NotNull CommandLine args) {
        var from = args.getOptionValue("path", "");
        Path pathFrom = Path.of(from).toAbsolutePath();
        var checksums = args.getOptionValue("checksums", "checksums.txt");
        Path pathChecksum = Path.of(checksums).toAbsolutePath();
        var algorithm = args.getOptionValue("algorithm", "sha256");
        boolean notFollowLinks = args.hasOption("not-follow-links");
        FileSystemBuilderApi fileSystemBuilder = notFollowLinks ?
                new FileSystemNotFollowingShortcutBuilder() : new FileSystemBuilder();
        ChecksumCalculator calc = ChecksumCalculator.create(algorithm);
        this(pathFrom, pathChecksum, calc, fileSystemBuilder);
    }

    public void pauseProcessing() {
        if (progressReporter != null && !progressReporter.isStopped() &&
                streamVisitor != null && !streamVisitor.isStopped()) {
            streamVisitor.pause();
            progressReporter.pause();

            processedFilesSnapshot = streamVisitor.getSnapshot();
            progressSnapshot = progressReporter.getSnapshot();
        }
    }

    public void resumeProcessing() {
        if (progressReporter != null && progressReporter.isStopped() &&
                streamVisitor != null && streamVisitor.isStopped() &&
                progressSnapshot != null && processedFilesSnapshot != null) {
            ProgressSnapshot curProgressSnapshot = progressSnapshot;
            ProcessedFilesSnapshot curProcessedFilesSnapshot = processedFilesSnapshot;

            progressReporter.restore(curProgressSnapshot);
            streamVisitor.restore(curProcessedFilesSnapshot);
            if (hasFinished()) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new UncheckedIOException("Error closing: " + outputStream, e);
                }

            }
            printEndTimer();
        }
    }

    @Override
    public void execute(@NotNull OutputStream fillStream) throws IOException {
        outputStream = fillStream;
        FileSystemEntity fileSystemEntity = fileSystemBuilder.build(from, checksums);
        fillOutputStreamWithChecksums(fileSystemEntity, fillStream);

        if (hasFinished()) {
            outputStream.close();
        }
    }

    @Override
    public boolean hasFinished() {
        return streamVisitor != null && streamVisitor.hasFinished();
    }

    @Override
    public void execute() {
        try {
            OutputStream outputStream = new FileOutputStream(checksums.toFile());
            execute(outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Error saving progress from calculation of checksums." , e);
        }
    }

    private void fillOutputStreamWithChecksums(@NotNull FileSystemEntity target, @NotNull OutputStream writerText) {
        HashStreamWriterVisitor hashStreamVisitor = new HashStreamWriterVisitor(
                writerText, calculator, FormatEditor.of(checksums));
        ReportWriterVisitor reportVisitor = new ReportWriterVisitor(System.out);

        this.streamVisitor = hashStreamVisitor;

        target.accept(reportVisitor);
        System.out.println();
        ProgressReporterMementoApi progressReporter = new ProgressReporter(System.out);
        hashStreamVisitor.attach(progressReporter);
        progressReporter.startTimer(target.getSize());

        this.progressReporter = progressReporter;

        target.accept(hashStreamVisitor);
        printEndTimer();

    }

    public void printEndTimer() {
        if (!streamVisitor.isStopped() && !progressReporter.isStopped()) {
            System.out.println(System.lineSeparator() + "Total time spent: " + progressReporter.endTimer()
                    + System.lineSeparator());
        }
    }
}
