package com.fmi.dp.labels;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomLabelProxy implements Label {
    private Label label;

    private final int timeout;
    private int currentTime;
    private static final int MIN_TIMEOUT_TIME = 1;

    public CustomLabelProxy(@NotNull Label label, int timeout) {
        this.label = label;
        this(timeout);
    }

    public CustomLabelProxy(int timeout) {
        if (timeout < MIN_TIMEOUT_TIME) {
            throw new IllegalArgumentException("Timeout time is too small!");
        }
        this.timeout = timeout;
        this.currentTime = 0;
    }

    private void changeText() {
        try {
            System.out.println("Input new text for the label!");
            String text = new BufferedReader(new InputStreamReader(System.in)).readLine();
            label = new SimpleTextLabel(text);

        } catch (IOException e) {
            throw new RuntimeException("Error while reading a text from console occurred.", e);
        }
    }

    @NotNull
    @Override
    public String getText() {
        if (label == null) {
            System.out.println("Label has not been initialized.");
            changeText();
            return label.getText();
        }
        currentTime++;
        if (currentTime == timeout) {
            timeout();
        }
        return label.getText();
    }

    public void timeout() {
        System.out.println("Would you like to change label? Y/N");
        try {
            String answer = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if (answer.equalsIgnoreCase("Y")) {
                changeText();
            }
            currentTime = 0;

        } catch (IOException e) {
            currentTime = 0;
            throw new RuntimeException("Error while reading a text from console occurred.", e);
        }
    }
}
