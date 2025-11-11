package com.fmi.dp.figures;

import java.math.BigDecimal;

public class Rectangle extends Figure {

    public Rectangle(BigDecimal a, BigDecimal b) {
        super(a, b);
    }

    @Override
    public BigDecimal calculatePerimeter() {
        if (calculatedPerimeter != null) {
            return calculatedPerimeter;
        }
        BigDecimal two = BigDecimal.valueOf(2);
        return calculatedPerimeter = two.multiply(lengths.get(0).add(lengths.get(1)));
    }
}
