package com.fmi.dp.commands.calculate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class VerifyCommandTest {
    private static final String STARTING_TEXT = "All checksums in text format:";
    private static final String ENDING_TEXT = "";
    private static final String FILE = "File: ";
    private static final String CHECKSUM = ", Checksum: ";

    private static File fileResult;

    private static final CalculateCommand calculateCommand = mock(CalculateCommand.class);
    private static Path checksumsPath;

    private static final PrintStream firstSout = System.out;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        fileResult = Files.createTempFile("someFile", "txt").toFile();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileResult));
        System.setOut(new PrintStream(outputStream));

        Path customDir =  Files.createTempDirectory("dirPath");
        Path tempFile = Files.createTempFile(customDir, "customTempChanged", ".txt");
        Path tempFile2 = Files.createTempFile(customDir, "customTempNotChanged", ".txt");
        Path tempFile3 = Files.createTempFile(customDir, "customTempInTheFirstChecksums", ".txt");
        Path tempFile4 = Files.createTempFile(customDir, "customTempInTheSecondChecksums", ".txt");

        checksumsPath = Files.createTempFile("pathWithChecksums", ".txt");
        File file = new File(String.valueOf(checksumsPath));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(STARTING_TEXT + System.lineSeparator());
            writer.write(FILE + tempFile + CHECKSUM + "123" + System.lineSeparator());
            writer.write(FILE + tempFile2 + CHECKSUM + "234" + System.lineSeparator());
            writer.write(FILE + tempFile3 + CHECKSUM + "345" + System.lineSeparator());
            writer.write(ENDING_TEXT);
        }

        when(calculateCommand.hasFinished()).thenReturn(false).thenReturn(true).thenReturn(true);
        doAnswer((invocation) -> {
            BufferedOutputStream stream = new BufferedOutputStream(invocation.getArgument(0));
            stream.write((STARTING_TEXT + System.lineSeparator() +
                    FILE + tempFile + CHECKSUM + "5a2" + System.lineSeparator() +
                    FILE + tempFile2 + CHECKSUM + "234" + System.lineSeparator() +
                    FILE + tempFile4 + CHECKSUM + "789" + System.lineSeparator() +
                    ENDING_TEXT
            ).getBytes());
            stream.flush();
            return null;
        }).when(calculateCommand).execute(any(OutputStream.class));
    }

    @Test
    void testVerifyCommand() throws IOException {
        VerifyCommand verifyCommand = new VerifyCommand(checksumsPath, Optional.empty(), calculateCommand);

        verifyCommand.execute();
        BufferedReader reader = new BufferedReader(new FileReader(fileResult));

        Set<String> states = new HashSet<>();
        for (int i = 0; i < 4; ++i) {
            String line = reader.readLine();
            if(line.contains("MODIFIED") && !states.contains("MODIFIED")) {
                assertTrue(line.contains("customTempChanged"));
                states.add("MODIFIED");
            } else if (line.endsWith("OK") && !states.contains("OK")) {
                assertTrue(line.contains("customTempNotChanged"));
                states.add("OK");
            } else if (line.endsWith("NEW") && !states.contains("NEW")) {
                assertTrue(line.contains("customTempInTheSecondChecksums"));
                states.add("NEW");
            } else if (line.endsWith("REMOVED") && !states.contains("REMOVED")) {
                assertTrue(line.contains("customTempInTheFirstChecksums"));
                states.add("REMOVED");
            } else {
                fail("Failed to recognize the 4 different states where each occurs once");
            }
        }

    }

    @AfterAll
    static void tearDownAfterClass() {
        System.setOut(firstSout);
    }
}
