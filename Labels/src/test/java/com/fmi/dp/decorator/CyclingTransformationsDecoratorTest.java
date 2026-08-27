package com.fmi.dp.decorator;

import com.fmi.labels.HelpLabel;
import com.fmi.labels.Label;
import com.fmi.labels.SimpleTextLabel;
import com.fmi.transformations.TextTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CyclingTransformationsDecoratorTest {
    private static LabelDecoratorBase decorator;

    @BeforeAll
    static void setUp() {
        TextTransformation tr1 = mock();
        TextTransformation tr2 = mock();
        TextTransformation tr3 = mock();

        when(tr1.transform("testing word")).thenReturn("testing word");
        when(tr2.transform("testing word")).thenReturn("Testing word");
        when(tr3.transform("testing word")).thenReturn("-={ testing word }=-");

        List<TextTransformation> textTransformations = List.of(tr1, tr2, tr3);
        Label mocked = mock();
        when(mocked.getText()).thenReturn("testing word");

        decorator = new CyclingTransformationsDecorator(mocked, textTransformations);
    }

    @Test
    void testCyclingTransformationsDecoratorConsistency() {
        assertEquals("testing word", decorator.getText());
        assertEquals("Testing word", decorator.getText());
        assertEquals("-={ testing word }=-", decorator.getText());
        assertEquals("testing word", decorator.getText());
        assertEquals("Testing word", decorator.getText());
        assertEquals("-={ testing word }=-", decorator.getText());
    }

    @Test
    void testEmptyTransformations() {
        assertThrows(IllegalArgumentException.class,
                () -> new CyclingTransformationsDecorator(new HelpLabel(new SimpleTextLabel("l"), "h"), List.of()),
                "Can't have empty transformations");
    }
}
