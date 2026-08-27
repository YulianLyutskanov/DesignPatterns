package com.fmi.dp.transformations.replace.censor;

import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CensorTransformationTest {
    private final TextTransformation textTransformation = new CensorTransformation("wow");

    String word = "Test length";
    @Test
    void testCensorTransformation() {

        assertEquals("woW one", textTransformation.transform("woW one"));
        assertEquals(" *** two", textTransformation.transform(" wow two"));
        assertEquals(word.length(), new CensorTransformation(word).transform(word).length());
    }
}
