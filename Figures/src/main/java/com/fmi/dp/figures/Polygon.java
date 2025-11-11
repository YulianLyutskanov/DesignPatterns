package com.fmi.dp.figures;

import java.math.BigDecimal;
import java.util.Collections;

public class Polygon extends Figure {

    public Polygon(BigDecimal... sides) {
        super(sides);
    }

    @Override
    public BigDecimal calculatePerimeter() {
        if (calculatedPerimeter != null) {
            return calculatedPerimeter;
        }
        calculatedPerimeter = lengths.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return calculatedPerimeter;
    }

    @Override
    protected void validateFigureExists() {
        calculatePerimeter();
        BigDecimal biggestSide = Collections.max(lengths);
        if (calculatedPerimeter.compareTo(biggestSide.multiply(BigDecimal.valueOf(2))) <= 0) {
            throw new IllegalArgumentException(
                "The figure is not valid, one of the sides is bigger than the sum of the rest of them!");
        }
    }
}
