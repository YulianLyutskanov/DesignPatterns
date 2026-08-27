package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

//dependency injection
public class TextTransformationDecorator extends LabelDecoratorBase {
    @NotNull
    private final TextTransformation textTransformation;

    public TextTransformationDecorator(@NotNull Label labelDecorator,
                                       @NotNull TextTransformation textTransformation) {
        super(labelDecorator);
        this.textTransformation = textTransformation;
    }

    @NotNull
    public TextTransformation getTextTransformation() {
        return textTransformation;
    }

    @Override
    public String getText() {
        return textTransformation.transform(super.getText());
    }
}
