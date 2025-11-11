package com.fmi.dp.figures;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TriangleTest {

    @Test
    void constructorInvalidParamsTest() {
        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(-1), BigDecimal.valueOf(3), BigDecimal.valueOf(6)),
            "Constructor should throw IllegalArgumentException when first side is negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(10), BigDecimal.valueOf(-3), BigDecimal.valueOf(6)),
            "Constructor should throw IllegalArgumentException when second side is negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(10), BigDecimal.valueOf(3), BigDecimal.valueOf(-6)),
            "Constructor should throw IllegalArgumentException when third side is negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(-10), BigDecimal.valueOf(-3), BigDecimal.valueOf(-6)),
            "Constructor should throw IllegalArgumentException when all sides are negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.ZERO, BigDecimal.valueOf(4), BigDecimal.valueOf(5)),
            "Constructor should throw IllegalArgumentException when a side is zero");
    }

    @Test
    void constructorInvalidTriangleRuleTest() {
        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3)),
            "Constructor should throw IllegalArgumentException when sides violate the triangle inequality (1,2,3)");

        assertThrows(IllegalArgumentException.class,
            () -> new Triangle(BigDecimal.valueOf(5), BigDecimal.valueOf(4), BigDecimal.valueOf(10)),
            "Constructor should throw IllegalArgumentException when sides violate the triangle inequality (5,4,10)");
    }

    @Test
    void constructorOkTest() {
        assertDoesNotThrow(() -> new Triangle(BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5)),
            "Constructor should NOT throw when sides form a valid triangle (3,4,5)");

        assertDoesNotThrow(() -> new Triangle(BigDecimal.valueOf(10), BigDecimal.valueOf(8), BigDecimal.valueOf(3)),
            "Constructor should NOT throw when sides form a valid triangle (10,8,3)");
    }

    @Test
    void calculatePerimeterIntegersTest() {
        Figure triangle = new Triangle(BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));

        assertEquals(BigDecimal.valueOf(12), triangle.calculatePerimeter(),
            "Perimeter should be the sum of all sides (3+4+5=12)");
    }

    @Test
    void calculatePerimeterDoublesTest() {
        Figure triangle = new Triangle(BigDecimal.valueOf(3.14), BigDecimal.valueOf(4.2), BigDecimal.valueOf(5.555));

        assertEquals(BigDecimal.valueOf(12.895), triangle.calculatePerimeter(),
            "Perimeter should be the sum of all sides (3+4+5=12)");
    }

    @Test
    void calculatePerimeterSymmetricTest() {
        Figure triangle1 = new Triangle(BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));
        Figure triangle2 = new Triangle(BigDecimal.valueOf(4), BigDecimal.valueOf(3), BigDecimal.valueOf(5));

        assertEquals(triangle1.calculatePerimeter(), triangle2.calculatePerimeter(),
            "Perimeters of congruent triangles be equal. (3,4,5) = (4,3,5)");
    }

    @Test
    void toStringTriangleTest() {
        BigDecimal sideA = BigDecimal.valueOf(5.123123);
        BigDecimal sideB = BigDecimal.valueOf(4.12);
        BigDecimal sideC = BigDecimal.valueOf(3.00012);
        Figure triangle = new Triangle(sideA, sideB, sideC);
        assertEquals("triangle 5.123123 4.12 3.00012", triangle.toString(),
            "Name of the class in string must be with lower case, name and sides separated with \" \" and sides should be saved with exact value!");
    }

    @Test
    void cloneTriangleTest() throws CloneNotSupportedException {
        BigDecimal sideA = BigDecimal.valueOf(5);
        BigDecimal sideB = BigDecimal.valueOf(12);
        BigDecimal sideC = BigDecimal.valueOf(13);

        Figure triangle = new Triangle(sideA, sideB, sideC);
        assertTrue(() -> triangle instanceof Cloneable,
            "When using clone the class must implement Cloneable");

        Figure cloned = triangle.clone();
        assertNotSame(triangle, cloned, "Cloned instance must be different from the original instance");
        assertEquals(triangle.toString(), cloned.toString(),
            "Strings of original and cloned must be same because toString is deterministic!");
    }
}
