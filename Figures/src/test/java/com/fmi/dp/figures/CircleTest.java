package com.fmi.dp.figures;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircleTest {

    @Test
    void constructorNegativeRadiusTest() {
        assertThrows(IllegalArgumentException.class, () -> new Circle(BigDecimal.valueOf(-1)),
            "Constructor should throw IllegalArgumentException when radius is negative");
    }

    @Test
    void constructorRadiusZeroTest() {
        assertThrows(IllegalArgumentException.class, () -> new Circle(BigDecimal.ZERO),
            "Constructor should throw IllegalArgumentException when radius is zero");
    }

    @Test
    void constructorRadiusPositiveTest() {
        assertDoesNotThrow(() -> new Circle(BigDecimal.valueOf(1.5)),
            "Constructor should NOT throw when radius is positive");
    }

    @Test
    void calculatePerimeterTest() {
        Figure circle = new Circle(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE.multiply(BigDecimal.TWO).multiply(BigDecimal.valueOf(Math.PI)),
            circle.calculatePerimeter(),
            "Perimeter should be 2 when radius is 1/pi (2*pi*r = 2*pi * (1/pi) = 2)");
    }

    @Test
    void toStringCircleTest() {
        BigDecimal radius = BigDecimal.valueOf(3.21142);
        Figure circle = new Circle(radius);
        assertEquals("circle 3.21142", circle.toString(),
            "Name of the class in string must be with lower case, name and radius separated with \" \" and radius should save the exact value!");
    }

    @Test
    void cloneCircleTest() throws CloneNotSupportedException {
        BigDecimal radius = BigDecimal.valueOf(2.3244142);
        Figure circle = new Circle(radius);
        assertTrue(() -> circle instanceof Cloneable,
            "When using clone the class must implement Cloneable");

        Figure cloned = circle.clone();
        assertNotSame(circle, cloned, "Cloned must be different from the original instance");
        assertEquals(circle.toString(), cloned.toString(),
            "Strings of original and cloned must be same because toString is deterministic!");
    }

}
