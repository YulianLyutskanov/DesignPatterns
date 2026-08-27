package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//does transformation on random index of the list
public class RandomTransformationDecorator extends LabelDecoratorBase {
    @NotNull
    private final List<@NotNull TextTransformation> textTransformations;

    public RandomTransformationDecorator(@NotNull Label label,
                                         @NotNull List<@NotNull TextTransformation> textTransformations) {
        super(label);
        this.textTransformations = textTransformations;
    }

    @Override
    public String getText() {
        int randomIdx = (int) (Math.random() * textTransformations.size());
        return textTransformations.get(randomIdx).transform(super.getText());
    }
}
