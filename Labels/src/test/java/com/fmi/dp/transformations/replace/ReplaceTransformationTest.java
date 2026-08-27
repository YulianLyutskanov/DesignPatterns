package com.fmi.dp.transformations.replace;

import com.fmi.transformations.TextTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplaceTransformationTest {
    private final TextTransformation textTransformation = new ReplaceTransformation("word", "new");
    @Test
    void testReplaceTransformation() {

        assertEquals(textTransformation.transform("word one"), "new one");
        assertEquals(textTransformation.transform(" Word two"), " Word two");
    }
}
