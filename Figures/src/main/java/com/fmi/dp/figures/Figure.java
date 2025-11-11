package com.fmi.dp.figures;

import com.fmi.dp.strings.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Figure implements Cloneable {
    protected List<BigDecimal> lengths;
    protected BigDecimal calculatedPerimeter = null;

    /**
     * @param lengths values of the sides
     * @throws IllegalArgumentException if one of the sides lengths is not a positive number
     * @throws IllegalArgumentException if one of the sides is bigger than the sum of the rest of them
     */
    public Figure(BigDecimal... lengths) {
        if (lengths == null || lengths.length == 0) {
            throw new IllegalArgumentException("Lengths cannot be null or empty");
        }
        this.lengths = new ArrayList<>();
        for (BigDecimal value : lengths) {
            validateSideLen(value);
            this.lengths.add(value);
        }
        validateFigureExists();
    }

    public abstract BigDecimal calculatePerimeter();

    /**
     * @throws IllegalArgumentException if one of the sides is bigger than the sum of the rest of them
     */
    protected void validateFigureExists() {
    }

    /**
     * @throws IllegalArgumentException if one of the sides lengths is not a positive number
     */
    private void validateSideLen(BigDecimal sideLen) {
        BigDecimal zero = BigDecimal.ZERO;
        if ((sideLen.compareTo(zero) <= 0)) {
            throw new IllegalArgumentException("Invalid len value, must be positive");
        }
    }

    @Override
    public String toString() {
        String classNameLower = StringUtils.lowercaseFirstLetter(this.getClass().getSimpleName());
        StringBuilder str = new StringBuilder(classNameLower);

        for (BigDecimal value : lengths) {
            str.append(" ").append(value);
        }
        return str.toString();
    }

    @Override
    public Figure clone() throws CloneNotSupportedException {
        Figure copy = (Figure) super.clone();
        copy.calculatedPerimeter = this.calculatedPerimeter;
        copy.lengths = new ArrayList<>(this.lengths);
        return copy;
    }

    public List<BigDecimal> getLengths() {
        return Collections.unmodifiableList(lengths);
    }

}
