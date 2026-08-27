package com.fmi.dp.transformations.whitespace;

import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalizeSpaceTransformationTest {
    private final TextTransformation textTransformation = new NormalizeSpaceTransformation();
    @Test
    void testNormalizeTransformation() {
        assertEquals(textTransformation.transform("       Word       one "), "Word one");
        assertEquals(textTransformation.transform("Word two"), "Word two");
    }
}
