package com.fmi.dp.figures;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RectangleTest {

    @Test
    void constructorInvalidParamsTest() {
        assertThrows(IllegalArgumentException.class,
            () -> new Rectangle(BigDecimal.valueOf(-1), BigDecimal.valueOf(3)),
            "Constructor should throw IllegalArgumentException when width is negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Rectangle(BigDecimal.valueOf(3), BigDecimal.valueOf(-1)),
            "Constructor should throw IllegalArgumentException when height is negative");

        assertThrows(IllegalArgumentException.class,
            () -> new Rectangle(BigDecimal.ZERO, BigDecimal.valueOf(3)),
            "Constructor should throw IllegalArgumentException when width is zero");
    }

    @Test
    void constructorOkTest() {
        assertDoesNotThrow(
            () -> new Rectangle(BigDecimal.valueOf(3), BigDecimal.valueOf(5)),
            "Constructor should NOT throw when width and height are positive (3,5)"
        );

        assertDoesNotThrow(
            () -> new Rectangle(BigDecimal.valueOf(5), BigDecimal.valueOf(2)),
            "Constructor should NOT throw when width and height are positive (5,2)"
        );
    }

    @Test
    void calculatePerimeterOkTest() {
        Rectangle rectangle1 = new Rectangle(BigDecimal.valueOf(3), BigDecimal.valueOf(5));
        assertEquals(
            BigDecimal.valueOf(16),
            rectangle1.calculatePerimeter(),
            "Perimeter should be 16 for rectangle with width 3 and height 5 (2*(3+5))"
        );

        Rectangle rectangle2 = new Rectangle(BigDecimal.valueOf(5), BigDecimal.valueOf(2));
        assertEquals(
            BigDecimal.valueOf(14),
            rectangle2.calculatePerimeter(),
            "Perimeter should be 14 for rectangle with width 5 and height 2 (2*(5+2))"
        );
    }

    @Test
    void calculatePerimeterSymmetricTest() {
        BigDecimal sideA = BigDecimal.valueOf(36.1251514121);
        BigDecimal sideB = BigDecimal.valueOf(33.12344828412);
        Figure rectangle = new Rectangle(sideA, sideB);
        Figure rectangle2 = new Rectangle(sideB, sideA);

        BigDecimal perimeter = rectangle.calculatePerimeter();
        BigDecimal perimeter2 = rectangle2.calculatePerimeter();

        assertEquals(perimeter, perimeter2, "Perimeters of symmetric rectangles must be equal.");
    }

    @Test
    void toStringRectangleTest() {
        BigDecimal sideA = BigDecimal.valueOf(23.267824142);
        BigDecimal sideB = BigDecimal.valueOf(Double.MAX_VALUE).divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP);
        Figure rectangle = new Rectangle(sideA, sideB);
        assertEquals("rectangle 23.267824142 " + sideB, rectangle.toString(),
            "Name of the class in string must be with lower case, name and sides separated with \" \" and side "
                + sideB + "should be saved with exact value!");
    }

    @Test
    void cloneRectangleTest() throws CloneNotSupportedException {
        BigDecimal sideA = BigDecimal.valueOf(3427.21343);
        BigDecimal sideB = BigDecimal.valueOf(234234.23423);

        Figure rectangle = new Rectangle(sideA, sideB);
        assertTrue(() -> rectangle instanceof Cloneable,
            "When using clone the class must implement Cloneable");

        Figure cloned = rectangle.clone();
        assertNotSame(rectangle, cloned, "Cloned instance is different from the original instance");
        assertEquals(rectangle.toString(), cloned.toString(),
            "Strings of original and cloned must be same because toString is deterministic!");
    }
}
