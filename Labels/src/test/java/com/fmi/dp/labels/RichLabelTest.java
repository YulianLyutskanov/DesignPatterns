package com.fmi.dp.labels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RichLabelTest {
    @Test
    public void testTextShouldReturnCorrectly()
    {
        // Arrange
        var expectedText = "test";
        Label label = new SimpleTextLabel(expectedText);
        var expectedColor = "red";
        var expectedFont = "Arial";
        int expectedFontSize = 16;
        Label labelRes = new RichLabel(label, expectedFontSize, expectedFont, expectedColor);

        // Act
        var actualText = labelRes.getText();

        // Assert
        assertTrue(actualText.contains(expectedText));
        assertTrue(actualText.contains(expectedColor));
        assertTrue(actualText.contains(expectedFont));
        assertTrue(actualText.contains(Integer.toString(expectedFontSize)));
    }
}
