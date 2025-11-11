package com.fmi.dp.factory;

import com.fmi.dp.exceptions.FigureCreationException;
import com.fmi.dp.figures.Circle;
import com.fmi.dp.figures.Figure;
import com.fmi.dp.figures.Rectangle;
import com.fmi.dp.figures.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringToFigureTest {
    @Test
    void createCircleFromValidStringTest() {
        String toStr = "circle 5.2124142";

        StringToFigure factoryFigure = new StringToFigure();
        Figure extracted = factoryFigure.createFrom(toStr);
        assertInstanceOf(Circle.class, extracted, "Should return a circle");
        assertEquals(toStr, extracted.toString(), "Must be valid with" + toStr);
    }

    @Test
    void createCircleFromStringNoRadiusTest() {
        String toStr = "circle";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "No radius given for circle!");
    }

    @Test
    void createCircleFromStringTwoParametersTest() {
        String toStr = "circle 23123.33 12312.2";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "Circle must have only one parameter!");
    }

    @Test
    void createCircleFromStringWhiteSpacesTest() {
        String toStr = "    circle     233423.33    ";
        StringToFigure factoryFigure = new StringToFigure();

        assertDoesNotThrow(() -> factoryFigure.createFrom(toStr));
        assertEquals(factoryFigure.createFrom(toStr).toString(), "circle 233423.33",
            "Should return circle 233423.33!");
    }

    @Test
    void createCircleFromStringWithNonDigitSymbolsTest() {
        String toStr = "circle 2354fd43";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "When extracting radius parameter should be valid BigDecimal!");
    }


    @Test
    void createRectangleFromValidStringTest() {
        String toStr = "rectangle 234.257642 4.12";

        StringToFigure factoryFigure = new StringToFigure();
        Figure extracted = factoryFigure.createFrom(toStr);

        assertInstanceOf(Rectangle.class, extracted, "Should return rectangle!");
        assertEquals(toStr, extracted.toString(), "Must be equal!");
    }

    @Test
    void createRectangleFromStringNoParametersTest() {
        String toStr = "rectangle ";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "Must take some parameters!");
    }

    @Test
    void createRectangleFromStringOneParametersTest() {
        String toStr = "rectangle 23123.33";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "Rectangle must have 2 parameters!");
    }

    @Test
    void createRectangleFromStringWhiteSpacesTest() {
        String toStr = "    rectangle     23123.33   233                 ";

        StringToFigure factoryFigure = new StringToFigure();
        assertDoesNotThrow(() -> factoryFigure.createFrom(toStr));
        assertEquals(factoryFigure.createFrom(toStr).toString(), "rectangle 23123.33 233",
            "Should return rectangle 23123.33 233!");
    }

    @Test
    void createRectangleFromStringNonDigitSymbolsTest() {
        String toStr = "rectangle 3ag23414 12412";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "When extracting radius parameter should be valid BigDecimal!");
    }

    @Test
    void createTriangleFromValidStringTest() {
        String toStr = "triangle 2.4574142 4.12 3";

        StringToFigure factoryFigure = new StringToFigure();
        Figure extracted = factoryFigure.createFrom(toStr);
        assertInstanceOf(Triangle.class, extracted, "Should return triangle!");
        assertEquals(toStr, extracted.toString(), "Must be equal!");
    }

    @Test
    void testUnknownCreateFromString() {
        String toStr = "unknown 2.2124142 4.12 3 2";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "unknown type does not exist!");
    }

    @Test
    void testDoubleCreateFromString() {
        String toStr = "double 2";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "Double class not found in the same directory as Figure directory!");
    }

    @Test
    void testFigureCreateFromString() {
        String toStr = "figure 2 4 3";

        StringToFigure factoryFigure = new StringToFigure();
        assertThrows(FigureCreationException.class, () -> factoryFigure.createFrom(toStr),
            "Creating instance of Figure should fail");
    }
}
