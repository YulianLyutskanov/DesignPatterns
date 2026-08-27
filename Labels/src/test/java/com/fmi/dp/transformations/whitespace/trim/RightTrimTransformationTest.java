package com.fmi.dp.transformations.whitespace.trim;

import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RightTrimTransformationTest {
    private final TextTransformation textTransformation = new RightTrimTransformation();
    @Test
    void testRightTrimTransformation() {

        assertEquals(textTransformation.transform("Word  one   "), "Word  one");
        assertEquals(textTransformation.transform("  word two  "), "  word two");
    }
}
