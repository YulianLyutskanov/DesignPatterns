package com.fmi.dp.transformations.composite;

import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeTransformationTest {
    private static TextTransformation textTransformation;

    @BeforeAll
    static void setUp() {
        TextTransformation tr1 = mock();
        TextTransformation tr2 = mock();
        TextTransformation tr3 = mock();

        when(tr1.transform("  testing word  ")).thenReturn("testing word");
        when(tr2.transform("testing word")).thenReturn("Testing word");
        when(tr3.transform("Testing word")).thenReturn("-={ Testing word }=-");

        textTransformation = new CompositeTransformation(List.of(tr1, tr2, tr3));
    }

    @Test
    void testCompositeTransformation() {
        assertEquals("-={ Testing word }=-", textTransformation.transform("  testing word  "));
    }
}
