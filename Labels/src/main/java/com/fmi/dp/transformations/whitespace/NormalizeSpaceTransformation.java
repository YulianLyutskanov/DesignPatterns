package com.fmi.dp.transformations.whitespace;

import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

public class NormalizeSpaceTransformation implements TextTransformation {
    @Override
    public String transform(@NotNull String text) {
        return text.replaceAll("\\s+", " ").strip();
    }
}
