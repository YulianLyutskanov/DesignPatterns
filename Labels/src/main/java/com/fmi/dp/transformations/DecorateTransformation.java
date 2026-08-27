package com.fmi.dp.transformations;

import org.jetbrains.annotations.NotNull;

public class DecorateTransformation implements TextTransformation {
    @NotNull
    private String decorationStarting;
    @NotNull
    private String decorationEnding;

    private static final String DEFAULT_DECORATION_STARTING = "-={";
    private static final String DEFAULT_DECORATION_ENDING = "}=-";

    public DecorateTransformation(@NotNull String decorationStarting, @NotNull String decorationEnding) {
        this.decorationStarting = decorationStarting;
        this.decorationEnding = decorationEnding;
    }

    public DecorateTransformation() {
        this.decorationStarting = DEFAULT_DECORATION_STARTING;
        this.decorationEnding = DEFAULT_DECORATION_ENDING;
    }

    public void setDecorationStarting(@NotNull String decorationStarting) {
        this.decorationStarting = decorationStarting;
    }

    public void setDecorationEnding(@NotNull String decorationEnding) {
        this.decorationEnding = decorationEnding;
    }

    @Override
    public String transform(@NotNull String text) {

        return decorationStarting + " " + text + " " + decorationEnding;
    }
}
