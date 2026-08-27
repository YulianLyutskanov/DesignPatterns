package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextTransformationDecoratorTest {
    private static LabelDecoratorBase decorator;

    @BeforeAll
    static void setUp() {
        TextTransformation tr1 = mock();

        when(tr1.transform("testing word")).thenReturn("-={ testing word }=-");

        Label mocked = mock();
        when(mocked.getText()).thenReturn("testing word");

        decorator = new TextTransformationDecorator(mocked, tr1);
    }

    @Test
    void testTextTransformationDecorator() {
        assertEquals("-={ testing word }=-", decorator.getText());
    }
}
