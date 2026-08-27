package com.fmi.dp.visitor.formateditors;

import com.fmi.dp.filesystem.FileSystemEntity;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFormatEditorTest {
    private static final String STARTING_TEXT = "All checksums in text format:";
    private static final String ENDING_TEXT = "";

    private static final String FILE = "File: ";
    private static final String CHECKSUM = ", Checksum: ";

    private final FormatEditor editor = new TextFormatEditor();

    @Test
    void testValidTextWrite() throws IOException {
        FileSystemEntity entity = mock(FileSystemEntity.class);
        when(entity.getAbsolutePath()).thenReturn(Path.of("somePath"));
        when(entity.getSize()).thenReturn(21L);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamWriter bufferedCriteria = new OutputStreamWriter(os);
        editor.writeChecksum(bufferedCriteria, entity, "34ab");
        bufferedCriteria.flush();

        StringReader stringReader = new StringReader(FILE + "somePath" + CHECKSUM + "34ab");
        BufferedReader bufferedStringReader = new BufferedReader(stringReader);

        BufferedReader inputReader =
                new BufferedReader(new InputStreamReader(new BufferedInputStream(new ByteArrayInputStream(os.toByteArray()))));
        assertEquals(bufferedStringReader.readLine(), inputReader.readLine());
    }

    private static InputStream stringReaderToInputStream(StringReader stringReader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        char[] buffer = new char[4096];
        int numCharsRead;
        while ((numCharsRead = stringReader.read(buffer)) != -1) {
            stringWriter.write(buffer, 0, numCharsRead);
        }
        byte[] byteArray = stringWriter.toString().getBytes();
        return new ByteArrayInputStream(byteArray);
    }

    @Test
    void testValidWrite() throws IOException {
        Map<Path, String> result = new HashMap<>();
        result.put(Path.of("somePath1"), "34ab");
        result.put(Path.of("somePath2"), "25cd");

        StringReader stringReader = new StringReader(STARTING_TEXT + System.lineSeparator() +
                FILE + "somePath1" + CHECKSUM + "34ab" + System.lineSeparator() +
                FILE + "somePath2" + CHECKSUM + "25cd" + System.lineSeparator() +
                ENDING_TEXT);
        Map<Path, String> shouldBe = editor.getChecksums(stringReaderToInputStream(stringReader));
        assertEquals(result, shouldBe);
    }
}
