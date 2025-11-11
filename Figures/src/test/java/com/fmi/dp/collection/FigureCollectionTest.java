package com.fmi.dp.collection;

import com.fmi.dp.exceptions.InvalidCommand;
import com.fmi.dp.factory.FigureFactory;
import com.fmi.dp.factory.RandomFigureFactory;
import com.fmi.dp.figures.Figure;
import com.fmi.dp.figures.Triangle;
import com.fmi.dp.figures.Circle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FigureCollectionTest {

    @Test
    void deleteOkTest() {
        FigureCollection figureCollection = new FigureCollection();
        FigureFactory factory = new RandomFigureFactory();
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());
        assertDoesNotThrow(() -> figureCollection.delete(1));
        assertEquals(figureCollection.size(), 2);
    }

    @Test
    void deleteOutOfBoundTest() {
        FigureCollection figureCollection = new FigureCollection();
        FigureFactory factory = new RandomFigureFactory();
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());
        assertThrows(InvalidCommand.class, () -> figureCollection.delete(7));
    }

    @Test
    void copyOkTest() {
        FigureCollection figureCollection = new FigureCollection();
        FigureFactory factory = new RandomFigureFactory();
        Figure first = factory.create();
        figureCollection.add(first);
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());

        assertDoesNotThrow(() -> figureCollection.copy(0));
        assertEquals(figureCollection.size(), 4);
        assertEquals(figureCollection.getFigures().getLast().getClass(), first.getClass());
    }

    @Test
    void copyOutOfBoundsTest() {
        FigureCollection figureCollection = new FigureCollection();
        FigureFactory factory = new RandomFigureFactory();
        Figure first = factory.create();
        figureCollection.add(first);
        figureCollection.add(factory.create());
        figureCollection.add(factory.create());

        assertThrows(InvalidCommand.class, () -> figureCollection.copy(8));
    }

    @Test
    void perimeterOkTest() {
        FigureCollection figureCollection = new FigureCollection();
        Figure c = new Circle(BigDecimal.ONE);
        figureCollection.add(c);

        assertDoesNotThrow(() -> figureCollection.perimeter(0));
    }

    @Test
    void perimeterOutOfBoundsTest() {
        FigureCollection figureCollection = new FigureCollection();
        Figure c = new Circle(BigDecimal.ONE);

        assertThrows(InvalidCommand.class, () -> figureCollection.perimeter(5));
    }

    @Test
    void getOkTest() throws CloneNotSupportedException {
        FigureCollection figureCollection = new FigureCollection();
        Figure c = new Circle(BigDecimal.ONE);
        figureCollection.add(c);

        assertDoesNotThrow(() -> figureCollection.get(0));
        assertEquals(figureCollection.get(0).getLengths(), c.getLengths());
    }

    @Test
    void getOutOfBoundsTest() throws CloneNotSupportedException {
        FigureCollection figureCollection = new FigureCollection();
        Figure c = new Circle(BigDecimal.ONE);
        figureCollection.add(c);

        assertNull(figureCollection.get(4));
    }

    @Test
    void serializeOkTest() {
        FigureCollection collection = new FigureCollection();
        Figure figure1 = new Circle(BigDecimal.ONE);
        Figure figure2 = new Triangle(BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));

        collection.add(figure1);
        collection.add(figure2);

        StringWriter writer = new StringWriter();
        collection.serialize(writer);

        String expected =
            "2" + System.lineSeparator() + figure1 + System.lineSeparator() + figure2 + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    void serializeThrowingTest() throws IOException {
        FigureCollection collection = new FigureCollection();
        Figure figure1 = new Circle(BigDecimal.ONE);
        Figure figure2 = new Triangle(BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));

        collection.add(figure1);
        collection.add(figure2);
        try (Writer throwingWriter = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {

            }

            @Override
            public void write(String string) throws IOException {
                throw new IOException("Simulated write failure");
            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        };) {
            assertThrows(RuntimeException.class, () -> collection.serialize(throwingWriter));
        }

    }

}