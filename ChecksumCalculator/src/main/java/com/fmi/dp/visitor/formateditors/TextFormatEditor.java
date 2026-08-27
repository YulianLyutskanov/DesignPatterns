package com.fmi.dp.visitor.formateditors;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TextFormatEditor implements FormatEditor {
    private static final String STARTING_TEXT = "All checksums in text format:";
    private static final String ENDING_TEXT = "";

    private static final String FILE = "File: ";
    private static final String CHECKSUM = ", Checksum: ";
    @Override
    public void writeChecksum(@NotNull OutputStreamWriter streamWriter,
                              @NotNull FileSystemEntity fileInfo, @NotNull String checksum) throws IOException {
        streamWriter.write( FILE + fileInfo.getAbsolutePath() + CHECKSUM + checksum + System.lineSeparator());
    }

    @Override
    public void writeChecksumAtBeginning(@NotNull OutputStreamWriter streamWriter,
                                         @NotNull ChecksumCalculator checksumCalculator)
            throws IOException {
        streamWriter.write( STARTING_TEXT + System.lineSeparator());
    }

    @Override
    public void writeChecksumAtEnd(@NotNull OutputStreamWriter streamWriter,
                                   @NotNull ChecksumCalculator checksumCalculator)
            throws IOException {
        streamWriter.write( ENDING_TEXT + System.lineSeparator());
        streamWriter.flush();
    }

    @Override
    public Map<Path, String> getChecksums(@NotNull InputStream inputStream) {
        Map<Path, String> checksums = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(FILE) && line.contains(CHECKSUM)) {
                    String[] parts = line.split(CHECKSUM);
                    String fileName = parts[0].replace(FILE, "").strip();
                    String checksum = parts[1].strip();

                    checksums.put(Path.of(fileName), checksum);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error reading: " + inputStream, e);
        }
        // Return the map containing file names and checksums
        return checksums;
    }
}
