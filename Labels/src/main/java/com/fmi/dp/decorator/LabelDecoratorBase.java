package com.fmi.dp.decorator;

import com.fmi.dp.labels.Label;
import com.fmi.dp.transformations.TextTransformation;
import org.jetbrains.annotations.NotNull;

public class LabelDecoratorBase implements Label {
    @NotNull
    private Label label;

    public LabelDecoratorBase(@NotNull Label label) {
        this.label = label;
    }

    @Override
    public String getText() {
        return label.getText();
    }

    public static Label removeDecoratorClassFrom(@NotNull Label label,
                                            @NotNull Class<? extends LabelDecoratorBase> decoratorClass) {
        if (label instanceof LabelDecoratorBase) {
            return ((LabelDecoratorBase) label).removeDecoratorClass(decoratorClass);
        }
        return label;
    }

    //make removeDecorator with a specific pointer decorator
    public Label removeDecoratorClass(@NotNull Class<? extends LabelDecoratorBase> decoratorType) {
        if (this.getClass().equals(decoratorType)) {
            return label;
        }
        if (!(label instanceof LabelDecoratorBase)) {
            return this;
        }
        label = ((LabelDecoratorBase) label).removeDecoratorClass(decoratorType);
        return this;

    }

    public static Label removeTransformationFrom(@NotNull Label label,
                                                      @NotNull TextTransformation transformation) {
        if (label instanceof LabelDecoratorBase) {
            return ((LabelDecoratorBase) label).removeTransformation(transformation);
        }
        return label;
    }

    public Label removeTransformation(@NotNull TextTransformation transformation) {
        if (this.getClass().equals(TextTransformationDecorator.class) &&
                ((TextTransformationDecorator)this).getTextTransformation().equals(transformation)) {
            return label;
        }
        if (!(label instanceof LabelDecoratorBase)) {
            return this;
        }
        label = ((LabelDecoratorBase) label).removeTransformation(transformation);
        return this;
    }

    public static Label removeTransformationClassFrom(@NotNull Label label,
                                            @NotNull Class<? extends TextTransformation> transformationClass) {
        if (label instanceof LabelDecoratorBase) {
            return ((LabelDecoratorBase) label).removeTransformationClass(transformationClass);
        }
        return label;
    }

    public Label removeTransformationClass(@NotNull Class<? extends TextTransformation> transformationClass) {
        if (this.getClass().equals(TextTransformationDecorator.class) &&
                ((TextTransformationDecorator)this).getTextTransformation().getClass().equals(transformationClass)) {
            return label;
        }
        if (!(label instanceof LabelDecoratorBase)) {
            return this;
        }
        label = ((LabelDecoratorBase) label).removeTransformationClass(transformationClass);
        return this;
    }
}
