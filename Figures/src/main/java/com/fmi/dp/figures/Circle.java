package com.fmi.dp.figures;

import java.math.BigDecimal;

public class Circle extends Figure {

    public Circle(BigDecimal radius) {
        super(radius);
    }

    @Override
    public BigDecimal calculatePerimeter() {
        if (calculatedPerimeter != null) {
            return calculatedPerimeter;
        }
        BigDecimal two = BigDecimal.valueOf(2);
        return calculatedPerimeter = two.multiply(BigDecimal.valueOf(Math.PI)).multiply(lengths.getFirst());
    }
}
