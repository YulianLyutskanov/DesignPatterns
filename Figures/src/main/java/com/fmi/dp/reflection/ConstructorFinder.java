package com.fmi.dp.reflection;

import com.fmi.dp.exceptions.FigureCreationException;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;

public class ConstructorFinder {
    public static Constructor<?> findRightConstructor(Class<?> clazz, int countParameters) {
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> containsVarArgs = null;
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (containsVarArgs == null && constructor.isVarArgs() &&
                Arrays.stream(parameterTypes).allMatch(type -> type == BigDecimal[].class)) {
                containsVarArgs = constructor;
            } else if (parameterTypes.length == countParameters &&
                Arrays.stream(parameterTypes).allMatch(type -> type == BigDecimal.class)) {
                return constructor;
            }
        }
        if (containsVarArgs != null) {
            return containsVarArgs;
        }
        throw new FigureCreationException("No " + clazz.getCanonicalName() + " constructor found");
    }

    public static Constructor<?> findRightConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> containsVarArgs = null;
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (containsVarArgs == null && constructor.isVarArgs() &&
                Arrays.stream(parameterTypes).allMatch(type -> type == BigDecimal[].class)) {
                containsVarArgs = constructor;
            }
            if (Arrays.stream(parameterTypes).allMatch(type -> type == BigDecimal.class)) {
                return constructor;
            }
        }
        if (containsVarArgs != null) {
            return containsVarArgs;
        }
        throw new IllegalArgumentException("No " + clazz.getCanonicalName() + " constructor found");
    }
}
