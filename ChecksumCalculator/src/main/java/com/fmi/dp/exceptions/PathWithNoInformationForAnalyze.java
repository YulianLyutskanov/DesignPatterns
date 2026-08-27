package com.fmi.dp.exceptions;

public class PathWithNoInformationForAnalyze extends RuntimeException {
    public PathWithNoInformationForAnalyze(String message) {
        super(message);
    }

    public PathWithNoInformationForAnalyze(String message, Throwable cause) {
        super(message, cause);
    }
}
