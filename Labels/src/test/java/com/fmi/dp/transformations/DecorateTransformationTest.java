package com.fmi.dp.transformations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecorateTransformationTest {
    private final TextTransformation textTransformation = new DecorateTransformation();
    private final TextTransformation textTransformationCustom = new DecorateTransformation("oO", "Oo");
    @Test
    void testDecorateTransformation() {

        assertEquals(textTransformation.transform("Word one"), "-={ Word one }=-");
        assertEquals(textTransformationCustom.transform("word two"), "oO word two Oo");
    }
}
