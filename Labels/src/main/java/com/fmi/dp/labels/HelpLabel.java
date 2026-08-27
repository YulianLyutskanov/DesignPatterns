package com.fmi.dp.labels;

import org.jetbrains.annotations.NotNull;

public class HelpLabel implements Label {
    @NotNull
    private final Label label;
    @NotNull
    private final String helpText;

    public HelpLabel(@NotNull Label label, @NotNull String helpText) {
        this.label = label;
        this.helpText = helpText;
    }

    @NotNull
    public String getHelpText() {
        return helpText;
    }

    @Override
    @NotNull
    public String getText() {
        return label.getText();
    }
}
