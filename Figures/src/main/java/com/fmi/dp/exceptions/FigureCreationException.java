package com.fmi.dp.exceptions;

public class FigureCreationException extends RuntimeException {
    public FigureCreationException() {
        super();
    }

    public FigureCreationException(String message) {
        super(message);
    }

    public FigureCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
