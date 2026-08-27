package com.fmi.dp.transformations.whitespace.trim;

import org.jetbrains.annotations.NotNull;

public class RightTrimTransformation implements TrimTransformation {
    @Override
    public String transform(@NotNull String text) {
        return text.stripTrailing();
    }
}
