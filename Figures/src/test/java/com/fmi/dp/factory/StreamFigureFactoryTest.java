package com.fmi.dp.factory;

import com.fmi.dp.exceptions.FigureCreationException;
import com.fmi.dp.figures.Circle;
import com.fmi.dp.figures.Figure;
import com.fmi.dp.figures.Triangle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StreamFigureFactoryTest {

    @Test
    void createFigureValidStreamTest() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("3.1352123124412123322312121312");
        BigDecimal bigDecimal2 = new BigDecimal("5.123");
        BigDecimal bigDecimal3 = new BigDecimal("6.223");
        BigDecimal radius = new BigDecimal("98.12");
        Figure triangle = new Triangle(bigDecimal, bigDecimal2, bigDecimal3);
        Figure circle = new Circle(radius);

        StringWriter writer = new StringWriter();
        writer.write(triangle + System.lineSeparator());
        writer.write(circle + System.lineSeparator());
        writer.flush();

        Reader reader = new StringReader(writer.toString());
        FigureFactory factory = new StreamFigureFactory(reader);
        assertEquals(factory.create().toString(), triangle.toString());
        assertEquals(factory.create().toString(), circle.toString());
    }

    @Test
    void createFigureEmptyStreamTest() throws IOException {
        StringReader reader = new StringReader(""); // empty content
        FigureFactory factory = new StreamFigureFactory(reader);
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read from empty file");
    }

    @Test
    void readMultipleTimesIfInvalidTest() throws IOException {
        StringReader reader = new StringReader("rectangle 3.2   "); // invalid figure
        FigureFactory factory = new StreamFigureFactory(reader);
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read from invalid file");
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read from invalid file twice");
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read from empty file third time");
    }

    @Test
    void createFigureWrongSyntaxStreamTest() throws IOException {
        StringReader reader = new StringReader("triangle 3 2"); // incomplete triangle
        FigureFactory factory = new StreamFigureFactory(reader);
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read from empty file");
    }

    @Test
    void createFigureNoSuchClassTest() throws IOException {
        StringReader reader = new StringReader("ivan 3 2");
        FigureFactory factory = new StreamFigureFactory(reader);
        assertThrows(FigureCreationException.class, factory::create,
            "Fail to read invalid figure type");
    }

    @Test
    void testStreamFigureFactoryNull() {
        assertThrows(IllegalArgumentException.class, () -> new StreamFigureFactory(null),
            "Can't put null in StreamFigureFactory");
    }
}
