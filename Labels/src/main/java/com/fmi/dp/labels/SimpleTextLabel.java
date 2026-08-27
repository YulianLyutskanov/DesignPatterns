package com.fmi.dp.labels;

import org.jetbrains.annotations.NotNull;

public class SimpleTextLabel implements Label {
    @NotNull
    protected final String text;

    public SimpleTextLabel(@NotNull String text) {
        this.text = text;
    }

    @Override
    @NotNull
    public String getText() {
        return text;
    }
}
