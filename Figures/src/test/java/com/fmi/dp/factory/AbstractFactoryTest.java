package com.fmi.dp.factory;

import com.fmi.dp.exceptions.InvalidCommand;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AbstractFactoryTest {

    @Test
    void randomFactoryTest() throws Exception {
        FigureFactory factory = AbstractFactory.createFactory("random");
        assertNotNull(factory, "Factory should not be null for 'random' input");
        assertInstanceOf(RandomFigureFactory.class, factory, "Factory should be instance of RandomFigureFactory");
    }

    @Test
    void randomFactoryScrambledTest() throws Exception {
        FigureFactory factory = AbstractFactory.createFactory("RAnDoM");
        assertNotNull(factory, "Factory should not be null for 'RAnDoM' input");
        assertInstanceOf(RandomFigureFactory.class, factory, "Factory should be instance of RandomFigureFactory");
    }

    @Test
    void systemInFactoryTest() throws Exception {
        FigureFactory factory = AbstractFactory.createFactory("systemin");
        assertNotNull(factory, "Factory should not be null for 'systemin' input");
        assertInstanceOf(StreamFigureFactory.class, factory, "Factory should be instance of StreamFigureFactory");
    }

    @Test
    void fileFactoryTest() throws Exception {
        Path tempFile = Files.createTempFile("figures", ".txt");
        Files.writeString(tempFile, "triangle 3 4 5");

        FigureFactory factory = AbstractFactory.createFactory("file " + tempFile.toAbsolutePath());
        assertNotNull(factory, "Factory should not be null when reading from file");
        assertInstanceOf(StreamFigureFactory.class, factory,
            "Factory should be instance of StreamFigureFactory when reading from file");

        tempFile.toFile().deleteOnExit();
    }

    @Test
    void emptySourceTest() {
        assertThrows(IllegalArgumentException.class,
            () -> AbstractFactory.createFactory(""),
            "Creating a factory with empty input should throw IllegalArgumentException");
    }

    @Test
    void unknownFactoryTypeTest() {
        assertThrows(InvalidCommand.class,
            () -> AbstractFactory.createFactory("unknownFactory"),
            "Creating a factory with unknown type should throw InvalidCommand");
    }

    @Test
    void fileWithoutFilenameTest() {
        assertThrows(IllegalArgumentException.class,
            () -> AbstractFactory.createFactory("file"),
            "Creating a file factory without filename should throw IllegalArgumentException");
    }
}
