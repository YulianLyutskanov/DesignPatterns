package com.fmi.dp.transformations.composite;

import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//Composite
public class CompositeTransformation implements TextTransformation {
    private final List<TextTransformation> transformations;

    public CompositeTransformation(@NotNull List<@NotNull TextTransformation> transformations) {
        this.transformations = transformations;
    }

    @Override
    public String transform(String text) {
        for (TextTransformation transformation : transformations) {
            text = transformation.transform(text);
        }
        return text;
    }
}
