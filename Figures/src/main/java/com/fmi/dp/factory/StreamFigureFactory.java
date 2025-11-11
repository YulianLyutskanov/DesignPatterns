package com.fmi.dp.factory;

import com.fmi.dp.exceptions.FigureCreationException;
import com.fmi.dp.figures.Figure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class StreamFigureFactory implements FigureFactory {
    private final BufferedReader bufferedReader;

    public StreamFigureFactory(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("reader is null");
        }
        this.bufferedReader = new BufferedReader(reader);
    }

    @Override
    public int countFigures() {
        try {
            String line = bufferedReader.readLine();
            return Integer.parseInt(line);
        } catch (Exception e) {
            throw new FigureCreationException("Invalid stream syntax");
        }
    }

    @Override
    public Figure create() {
        try {
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                return (new StringToFigure()).createFrom(line);
            }
        } catch (IOException e) {
            throw new FigureCreationException(e.getMessage(), e.getCause());
        }
        throw new FigureCreationException("No figure found");
    }
}
