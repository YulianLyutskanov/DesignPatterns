package com.fmi.dp.transformations.replace.censor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CensorTransformationFactoryTest {
    @Test
    void testCensorTransformationFactory() {
        assertEquals(CensorTransformationFactory.createCensorTransformation("NotOkay").getCensoredText(),
                new CensorTransformation("NotOkay").getCensoredText());
    }
}
