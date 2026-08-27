package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CyclingTransformationsDecorator extends LabelDecoratorBase {
    @NotNull
    private final List<@NotNull TextTransformation> textTransformations;
    private int cyclingIndex = 0;

    public CyclingTransformationsDecorator(@NotNull Label label,
                                           @NotNull List<TextTransformation> textTransformations) {
        if (textTransformations.isEmpty()) {
            throw new IllegalArgumentException("Text transformations can't be empty");
        }
        super(label);
        this.textTransformations = textTransformations;
    }

    @Override
    public String getText() {
        String text = super.getText();
        text = textTransformations.get(cyclingIndex++).transform(text);
        cyclingIndex %= textTransformations.size();
        return text;
    }
}
