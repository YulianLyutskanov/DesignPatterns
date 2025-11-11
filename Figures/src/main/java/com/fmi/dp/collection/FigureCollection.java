package com.fmi.dp.collection;

import com.fmi.dp.exceptions.InvalidCommand;
import com.fmi.dp.figures.Figure;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FigureCollection implements Serializable {
    private final List<Figure> figures;

    public FigureCollection() {
        figures = new ArrayList<>();
    }

    public void add(Figure figure) {
        figures.add(figure);
    }

    public int size() {
        return figures.size();
    }

    @Override
    public void serialize(Writer writer) {
        try {
            writer.write(figures.size() + System.lineSeparator());
            for (Figure figure : figures) {
                writer.write(figure.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write the figures to the stream", e);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not flush the stream", e);
        }

    }

    public void delete(int idxDelete) {
        if (idxDelete >= 0 && idxDelete < figures.size()) {
            figures.remove(idxDelete);
        } else {
            throw new InvalidCommand("Index out of bounds, must be between 0 and " + (figures.size() - 1));
        }
    }

    public void copy(int idxCopy) throws CloneNotSupportedException {
        if (idxCopy >= 0 && idxCopy < figures.size()) {
            figures.add(figures.get(idxCopy).clone());
        } else {
            throw new InvalidCommand("Index out of bounds, must be between 0 and " + (figures.size() - 1));
        }
    }

    public BigDecimal perimeter(int idxCopy) {
        if (idxCopy >= 0 && idxCopy < figures.size()) {
            return figures.get(idxCopy).calculatePerimeter();
        } else {
            throw new InvalidCommand("Index out of bounds, must be between 0 and " + (figures.size() - 1));
        }
    }

    public Figure get(int idx) throws CloneNotSupportedException {
        if (idx >= 0 && idx < figures.size()) {
            return figures.get(idx).clone();
        } else {
            return null;
        }
    }

    public List<Figure> getFigures() {
        return Collections.unmodifiableList(figures);
    }
}
