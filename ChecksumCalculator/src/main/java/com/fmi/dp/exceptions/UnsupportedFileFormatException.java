package com.fmi.dp.exceptions;

public class UnsupportedFileFormatException extends RuntimeException {
    public UnsupportedFileFormatException(String message) {
        super(message);
    }

    public UnsupportedFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
