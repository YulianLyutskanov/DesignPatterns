package com.fmi.dp.exceptions;

public class InvalidAlgorithmException extends RuntimeException {
    public InvalidAlgorithmException(String message) {
        super(message);
    }

    public InvalidAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
