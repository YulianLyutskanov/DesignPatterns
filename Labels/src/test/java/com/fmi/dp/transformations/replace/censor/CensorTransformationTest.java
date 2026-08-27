package com.fmi.dp.transformations.replace.censor;

import com.fmi.transformations.CapitalizeTransformation;
import com.fmi.transformations.TextTransformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CensorTransformationTest {
    private final TextTransformation textTransformation = new CensorTransformation("wow");

    String word = "Test length";
    @Test
    void testCensorTransformation() {

        assertEquals(textTransformation.transform("woW one"), "woW one");
        assertEquals(textTransformation.transform(" wow two"), " *** two");
        assertEquals(word.length(), new CensorTransformation(word).transform(word).length());
    }
}
