package com.fmi.dp.writer;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

public abstract class BaseStreamWriter implements Closeable {
    @NotNull
    protected final OutputStreamWriter streamWriter;

    public BaseStreamWriter(@NotNull OutputStream outputStream) {
        this.streamWriter = new OutputStreamWriter(outputStream);
    }

    @Override
    public void close() {
        try (streamWriter) {
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while closing BaseObservableStreamWriter", e);
        }
    }
}
