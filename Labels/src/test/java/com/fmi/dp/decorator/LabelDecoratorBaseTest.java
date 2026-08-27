package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.CapitalizeTransformation;
import com.fmi.dp.transformations.DecorateTransformation;
import com.fmi.dp.transformations.TextTransformation;
import com.fmi.dp.transformations.composite.CompositeTransformation;
import com.fmi.dp.transformations.whitespace.trim.LeftTrimTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LabelDecoratorBaseTest {
    private static Label mocked;
    private static List<TextTransformation> transformations;

    @BeforeAll
    static void setUp() {
        TextTransformation tr1 = mock(LeftTrimTransformation.class);
        TextTransformation tr2 = mock(CapitalizeTransformation.class);
        TextTransformation tr3 = mock(DecorateTransformation.class);

        when(tr1.transform(" testing word")).thenReturn("testing word");
        when(tr2.transform("testing word")).thenReturn("Testing word");
        when(tr2.transform(" testing word")).thenReturn(" testing word");
        when(tr3.transform("Testing word")).thenReturn("-={ Testing word }=-");
        when(tr3.transform("testing word")).thenReturn("-={ testing word }=-");
        when(tr3.transform(" testing word")).thenReturn("-={  testing word }=-");

        transformations = List.of(tr1, tr2, tr3);
        mocked = mock();
        when(mocked.getText()).thenReturn(" testing word");
    }

    @Test
    void testRemovingConcreteClassTextDecorators() {
        Label toDecorate = mocked; // " testing word"
        when(toDecorate.getText()).thenReturn(" testing word");
        toDecorate = new TextTransformationDecorator(toDecorate, transformations.get(0));
        assertEquals("testing word", toDecorate.getText());
        toDecorate = new TextTransformationDecorator(toDecorate, transformations.get(1));
        assertEquals("Testing word", toDecorate.getText());
        toDecorate = new TextTransformationDecorator(toDecorate, transformations.get(2));
        assertEquals("-={ Testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, CapitalizeTransformation.class);
        assertEquals("-={ testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, LeftTrimTransformation.class);
        assertEquals("-={  testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, CompositeTransformation.class);
        assertEquals("-={  testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, DecorateTransformation.class);
        assertEquals(" testing word", toDecorate.getText());

        //now should return at the moment because toDecorate is not LabelDecoratorBase, its Label
        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, DecorateTransformation.class);
        assertEquals(" testing word", toDecorate.getText());
    }

    @Test
    void testRemovingClassDecorators() {
        Label toDecorate = mocked; // " testing word"
        when(toDecorate.getText()).thenReturn(" testing word");

        toDecorate = new TextTransformationDecorator(toDecorate, transformations.get(0));
        assertEquals("testing word", toDecorate.getText());

        toDecorate = new CyclingTransformationsDecorator(toDecorate, List.of(transformations.get(2)));
        assertEquals("-={ testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationClassFrom(toDecorate, CapitalizeTransformation.class);
        assertEquals("-={ testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeDecoratorClassFrom(toDecorate, TextTransformationDecorator.class);
        assertEquals("-={  testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeDecoratorClassFrom(toDecorate, RandomTransformationDecorator.class);
        assertEquals("-={  testing word }=-", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeDecoratorClassFrom(toDecorate, CyclingTransformationsDecorator.class);
        assertEquals(" testing word", toDecorate.getText());

        //now should return at the moment because toDecorate is not LabelDecoratorBase, its Label
        toDecorate = LabelDecoratorBase.removeDecoratorClassFrom(toDecorate, CyclingTransformationsDecorator.class);
        assertEquals(" testing word", toDecorate.getText());
    }

    @Test
    void testRemovingTransformation() {
        Label toDecorate = mocked; // " testing word"
        when(toDecorate.getText()).thenReturn(" testing word");

        toDecorate = new TextTransformationDecorator(toDecorate, transformations.get(0));
        assertEquals("testing word", toDecorate.getText());

        toDecorate = LabelDecoratorBase.removeTransformationFrom(toDecorate, new LeftTrimTransformation());
        assertEquals("testing word", toDecorate.getText(),
                "remove with the same class a the transformation should not apply if they are not with the same" +
                        "reference");

        toDecorate = LabelDecoratorBase.removeTransformationFrom(toDecorate, transformations.get(0));
        assertEquals(" testing word", toDecorate.getText(),
                "removing with the same transformation reference, should apply removing");
    }
}
