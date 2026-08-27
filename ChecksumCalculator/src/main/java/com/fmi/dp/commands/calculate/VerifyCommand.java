package com.fmi.dp.commands.calculate;

import com.fmi.dp.visitor.formateditors.FormatEditor;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VerifyCommand implements ModeCommand {
    @NotNull
    private final Path checksums;
    @NotNull
    private final OutputStream pathTo;
    @NotNull
    private final CalculateCommand calculateCommand;

    private final ByteArrayOutputStream resultCalculator = new ByteArrayOutputStream();

    private static final String NEW = "NEW";
    private static final String MODIFIED = "MODIFIED";
    private static final String REMOVED = "REMOVED";
    private static final String OK = "OK";

    public VerifyCommand(@NotNull Path checksums,@NotNull Optional<Path> pathTo,
                         @NotNull CalculateCommand calculateCommand) {
        this.checksums = checksums;
        try {
            this.pathTo = pathTo.isPresent() ? new FileOutputStream(pathTo.get().toFile()) : System.out;
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }

        this.calculateCommand = calculateCommand;
    }

    public VerifyCommand(@NotNull CommandLine args) {
        var calculateCommand = new CalculateCommand(args);
        Optional<Path> pathTo = Optional.empty();
        if (args.hasOption("pathto")) {
            pathTo = Optional.of(Path.of(args.getOptionValue("pathto")).toAbsolutePath());
        }

        var checksums = args.getOptionValue("checksums", "checksums.txt");
        Path checksumsPath = Path.of(checksums).toAbsolutePath();

        this(checksumsPath, pathTo, calculateCommand);
    }

    @Override
    public boolean hasFinished() {
        return calculateCommand.hasFinished();
    }

    @Override
    public void execute() {
        try {
            execute(pathTo);
            if (!pathTo.equals(System.out) && calculateCommand.hasFinished()) {
                try (pathTo) { }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error with while working with files." , e);
        }
    }

    /**
     * not closing fillStream because it is created outside the method
     */
    @Override
    public void execute(@NotNull OutputStream fillStream) throws IOException {
        try {
            if (!calculateCommand.hasFinished()) {
                calculateCommand.execute(resultCalculator);
            }
            if (!calculateCommand.hasFinished()) {
                return;
            }
            FormatEditor formatEditor = FormatEditor.of(checksums);
            Map<Path, String> checksumsSource = formatEditor.getChecksums(
                    new BufferedInputStream(new ByteArrayInputStream(resultCalculator.toByteArray())));

            Map<Path, String> checksumsCriteria;
            if (Files.exists(checksums)) {
                checksumsCriteria = formatEditor.getChecksums(
                        new BufferedInputStream(new FileInputStream(checksums.toFile())));
            } else {
                checksumsCriteria = new HashMap<>();
            }


            BufferedOutputStream resultFillStream = new BufferedOutputStream(fillStream);
            writeResultValidate(resultFillStream, checksumsSource, checksumsCriteria);
        } catch (IOException e) {
            throw new UncheckedIOException("Problem with:" + fillStream, e);
        }
    }

    @Override
    public void resumeProcessing() {
        calculateCommand.resumeProcessing();
        execute();
    }

    @Override
    public void pauseProcessing() {
         calculateCommand.pauseProcessing();
    }

    private void writeResultValidate(@NotNull OutputStream outputStream,
                                     @NotNull Map<Path, String> source,
                                     @NotNull Map<Path, String> criteria)
            throws IOException {
        for (Map.Entry<Path, String> entry : source.entrySet()) {
            if (!criteria.containsKey(entry.getKey())) {
                outputStream.write((entry.getKey() + ": " + NEW + System.lineSeparator()).getBytes());
                continue;
            } else if (entry.getValue().equals(criteria.get(entry.getKey()))) {
                outputStream.write((entry.getKey() + ": " + OK + System.lineSeparator()).getBytes());
            } else {
                outputStream.write((entry.getKey() + ": " + MODIFIED + System.lineSeparator()).getBytes());
            }
            criteria.remove(entry.getKey());
        }
        for (Path entry : criteria.keySet()) {
            outputStream.write((entry + ": " + REMOVED + System.lineSeparator()).getBytes());
        }
        outputStream.flush();
    }
}
