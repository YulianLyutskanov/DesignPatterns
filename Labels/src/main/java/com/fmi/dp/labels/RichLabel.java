package com.fmi.dp.labels;

import org.jetbrains.annotations.NotNull;

public class RichLabel implements Label {
    private final int fontSize;
    @NotNull
    private final String fontFamily;
    @NotNull
    private final String color;
    @NotNull
    private final Label label;

    public RichLabel(@NotNull Label label, int fontSize, @NotNull String fontFamily, @NotNull String color) {
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.color = color;
        this.label = label;
    }

    @Override
    @NotNull
    public String getText() {
        return "<html><font color=\"" + color +
                "\" face=\"" + fontFamily +
                "\" size=\"" + fontSize + "\">" + label.getText() + "</font></html>";
    }
}
