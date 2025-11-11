package com.fmi.dp.factory;

import com.fmi.dp.figures.Figure;
import com.fmi.dp.reflection.FigurePackageName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomFigureFactoryTest {

    private static final int N = 300;
    private static final BigDecimal MAX = BigDecimal.valueOf(10_000);
    private static RandomFigureFactory factory = new RandomFigureFactory();

    @Test
    void figureCreationDoesNotThrowTest() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < N; i++) {
                Figure f = factory.create();
                assertNotNull(f, "Created figure should not be null");
            }
        }, "All figures must be successfully created");
    }

    @Test
    void testFigureTypeDistribution() {
        Map<Class<?>, Integer> counts = new HashMap<>();

        for (int i = 0; i < N; i++) {
            Figure figure = factory.create();
            counts.put(figure.getClass(), counts.getOrDefault(figure.getClass(), 0) + 1);
        }


        Reflections reflections = new Reflections(FigurePackageName.getPackageName());
        Set<Class<? extends Figure>> subclasses = reflections.getSubTypesOf(Figure.class);
        assertTrue(counts.keySet().containsAll(subclasses));

        double average = counts.values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElseThrow(() -> new AssertionError("No figures generated"));


        double tolerance = 0.4;
        for (Map.Entry<Class<?>, Integer> entry : counts.entrySet()) {
            double ratio = entry.getValue() / average;
            assertTrue(
                ratio > 1 - tolerance && ratio < 1 + tolerance,
                String.format(
                    "Figure type %s appears %d times (ratio %.2f) — outside expected range (±%.0f%%)",
                    entry.getKey().getSimpleName(), entry.getValue(), ratio, tolerance * 100
                )
            );
        }
    }
}
