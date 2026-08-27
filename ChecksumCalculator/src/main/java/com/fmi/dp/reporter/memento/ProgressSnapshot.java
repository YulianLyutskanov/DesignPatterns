package com.fmi.dp.reporter.memento;

public record ProgressSnapshot(long readBytes, long expectedBytesToRead, long elapsedMilliseconds) {
}
