package com.fmi.dp.visitor.formateditors;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Map;

public interface FormatEditor {
    void writeChecksum(@NotNull OutputStreamWriter streamWriter,
                       @NotNull FileSystemEntity fileInfo, @NotNull String checksum) throws IOException;

    void writeChecksumAtBeginning(@NotNull OutputStreamWriter streamWriter,
                                  @NotNull ChecksumCalculator checksumCalculator) throws IOException;

    void writeChecksumAtEnd(@NotNull OutputStreamWriter streamWriter,
                            @NotNull ChecksumCalculator checksumCalculator) throws IOException;

    Map<Path, String> getChecksums(@NotNull InputStream inputStream);

    static FormatEditor of(@NotNull Path format) {
        String formatName = format.toString().toLowerCase().strip();
        if (formatName.endsWith("txt")) {
            return new TextFormatEditor();
        } else if (formatName.endsWith("xml")) {
            return new XmlFormatEditor();
        } else {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}
