package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomTransformationDecoratorTest {
    private static final Map<String, Integer> occurrence = new HashMap<>();
    private static final int MIN_DISTRIBUTION_OCCURRENCE = 10;

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

        LabelDecoratorBase decorator = new RandomTransformationDecorator(mocked, textTransformations);

        occurrence.put("testing word", 0);
        occurrence.put("Testing word", 0);
        occurrence.put("-={ testing word }=-", 0);
        for (int i = 0; i < 1000; ++i) {
            String currentText = decorator.getText();
            occurrence.put(currentText, occurrence.get(currentText) + 1);
        }
    }

    @Test
    void testDistributionRandomDecorator() {
        for (Map.Entry<String, Integer> entry : occurrence.entrySet()) {
            assertTrue(entry.getValue() > MIN_DISTRIBUTION_OCCURRENCE,
                    "Testing 1000 random values with 3 possibilities, " +
                            "each one of them should have a distribution of at least 10.");
        }
    }
}
