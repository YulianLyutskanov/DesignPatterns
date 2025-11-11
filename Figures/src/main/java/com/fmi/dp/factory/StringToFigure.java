package com.fmi.dp.factory;

import com.fmi.dp.figures.Figure;
import com.fmi.dp.reflection.ConstructorFinder;
import com.fmi.dp.reflection.FigurePackageName;
import com.fmi.dp.exceptions.FigureCreationException;
import com.fmi.dp.strings.StringUtils;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringToFigure {
    public Figure createFrom(String representation) {
        if (representation == null || representation.isBlank()) {
            throw new IllegalArgumentException("Cannot convert null or empty string to figure");
        }
        try {
            Scanner scanner = new Scanner(representation);
            String className = getNameOfFileLocation(scanner.next());
            Class<?> clazz = Class.forName(className);

            validateSubclassOfFigure(clazz);

            List<BigDecimal> hold = new ArrayList<>();
            while (scanner.hasNext()) {
                BigDecimal curr = scanner.nextBigDecimal();
                hold.add(curr);
            }

            Constructor<?> constructor = ConstructorFinder.findRightConstructor(clazz, hold.size());
            if (constructor.isVarArgs()) {
                return (Figure) constructor.newInstance((Object) hold.toArray(BigDecimal[]::new));
            }
            return (Figure) constructor.newInstance(hold.toArray());
        } catch (Exception e) {
            throw new FigureCreationException("Couldn't create figure from " + representation, e);
        }

    }

    private String getNameOfFileLocation(String ofClassName) {
        return FigurePackageName.getPackageName() + "." + StringUtils.capitalizeFirstLetter(ofClassName);
    }

    private void validateSubclassOfFigure(Class<?> clazz) {
        if (!Figure.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class is not a figure");
        }
    }
}

