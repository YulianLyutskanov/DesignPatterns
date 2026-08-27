package com.fmi.dp.transformations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CapitalizeTransformationTest {
    private final TextTransformation textTransformation = new CapitalizeTransformation();
    @Test
    void testCapitalizeTransformation() {

        assertEquals(textTransformation.transform("word one"), "Word one");
        assertEquals(textTransformation.transform(" word two"), " word two");
    }
}
