package com.fmi.dp.factory;

import com.fmi.dp.exceptions.FigureCreationException;
import com.fmi.dp.figures.Figure;
import com.fmi.dp.figures.Polygon;
import com.fmi.dp.reflection.ConstructorFinder;
import com.fmi.dp.reflection.FigurePackageName;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFigureFactory implements FigureFactory {

    private static final BigDecimal MAX = BigDecimal.valueOf(10_000); // max value for sides
    private static final int SCALE = 4; // number of decimal places
    private static final int MIN_VARARGS = 4;
    private static final int MAX_VARARGS = 10;
    private static final Random RANDOM = new Random();

    private static final List<Class<? extends Figure>> FIGURE_CLASSES =
        Collections.unmodifiableList(initFigureClasses());

    private static List<Class<? extends Figure>> initFigureClasses() {
        Reflections reflections = new Reflections(FigurePackageName.getPackageName());
        Set<Class<? extends Figure>> subclasses = reflections.getSubTypesOf(Figure.class);
        subclasses.remove(Figure.class);
        return new ArrayList<>(subclasses);
    }

    @Override
    public Figure create() {
        try {
            Class<? extends Figure> randomClass = FIGURE_CLASSES.get(RANDOM.nextInt(FIGURE_CLASSES.size()));
            Constructor<?> constructor = ConstructorFinder.findRightConstructor(randomClass);

            if (constructor.isVarArgs()) {
                return createVarArgs(randomClass, constructor);
            } else {
                return createFixedArgs(randomClass, constructor);
            }
        } catch (Exception e) {
            throw new FigureCreationException("Failed to create random figure", e);
        }
    }

    @Override
    public int countFigures() {
        System.out.println("Input number of random figures to be generated!");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private Figure createFixedArgs(Class<?> clazz, Constructor<?> constructor) throws Exception {
        int paramCount = constructor.getParameterTypes().length;
        List<BigDecimal> args = Polygon.class.isAssignableFrom(clazz)
            ? randPolygonList(paramCount)
            : randBigDecimalList(paramCount);
        return (Figure) constructor.newInstance(args.toArray());
    }

    private Figure createVarArgs(Class<?> clazz, Constructor<?> constructor) throws Exception {
        int paramCount = RANDOM.nextInt(MAX_VARARGS - MIN_VARARGS + 1) + MIN_VARARGS;
        List<BigDecimal> args = Polygon.class.isAssignableFrom(clazz)
            ? randPolygonList(paramCount)
            : randBigDecimalList(paramCount);
        return (Figure) constructor.newInstance((Object) args.toArray(new BigDecimal[0]));
    }

    //This method is written with AI

    /**
     * Generates a list of random BigDecimals for a polygon that satisfies the polygon inequality
     */
    private List<BigDecimal> randPolygonList(int count) {
        List<BigDecimal> sides = new ArrayList<>();

        // Generate first count - 1 sides randomly
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < count - 1; i++) {
            BigDecimal side = randBigDecimal();
            sides.add(side);
            sum = sum.add(side);
        }

        // Find the largest side among the first ones
        BigDecimal maxSide = Collections.max(sides);

        // Compute valid interval for last side to satisfy polygon inequality
        BigDecimal minLast = maxSide.multiply(BigDecimal.valueOf(2)).subtract(sum);
        if (minLast.compareTo(BigDecimal.ZERO) < 0) {
            minLast = BigDecimal.ZERO;
        }
        BigDecimal maxLast = sum;

        // Randomize last side within interval
        BigDecimal lastSide = minLast.add(
            BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()).multiply(maxLast.subtract(minLast))
        ).setScale(SCALE, RoundingMode.HALF_UP);

        sides.add(lastSide);
        Collections.shuffle(sides, RANDOM); // Shuffle to avoid predictable order
        return sides;
    }

    /**
     * Generates a list of fully random BigDecimals list between the configured values
     */
    private List<BigDecimal> randBigDecimalList(int count) {
        List<BigDecimal> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(randBigDecimal());
        }
        return list;
    }

    /**
     * Generates a random BigDecimal in (0, MAX) with fixed scale
     */
    private BigDecimal randBigDecimal() {
        return BigDecimal.valueOf(Math.random())
            .multiply(MAX)
            .setScale(SCALE, RoundingMode.HALF_UP);
    }
}
