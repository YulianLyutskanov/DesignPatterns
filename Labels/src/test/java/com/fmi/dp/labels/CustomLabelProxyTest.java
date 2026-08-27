package com.fmi.dp.labels;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomLabelProxyTest {
    @Test
    void testTimeoutConsistency() {
        CustomLabelProxy customLabel = spy(new CustomLabelProxy(2)); // Timeout set to 2

        System.setIn(new ByteArrayInputStream(("label text").getBytes()));

        for (int i = 0; i < 5; i++) {
            assertEquals(customLabel.getText(),
                    "label text", "If option: \"Not change\" is selected, text should not change.");
            System.setIn(new ByteArrayInputStream(("no").getBytes()));
        }
        verify(customLabel, times(2)).timeout();
    }

    @Test
    void testNegativeTimeoutConsistency() {
        assertThrows(IllegalArgumentException.class, () -> new CustomLabelProxy(-1),
                "Timeout can't be negative");
    }
}
