package com.fmi.dp.transformations.replace;

import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

public class ReplaceTransformation implements TextTransformation {
    @NotNull
    private final String toReplaceText;
    @NotNull
    private final String replacingWithText;

    public ReplaceTransformation(@NotNull String toReplaceText, @NotNull String replacingWithText) {
        this.toReplaceText = toReplaceText;
        this.replacingWithText = replacingWithText;
    }

    @Override
    public String transform(@NotNull String text) {
        return text.replaceAll(toReplaceText, replacingWithText);
    }
}
