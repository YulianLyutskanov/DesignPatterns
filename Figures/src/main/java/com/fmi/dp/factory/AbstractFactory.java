package com.fmi.dp.factory;

import com.fmi.dp.exceptions.InvalidCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class AbstractFactory {
    public static FigureFactory createFactory(String typeFactory) throws FileNotFoundException {
        Scanner scanner = new Scanner(typeFactory);
        if (!scanner.hasNext()) {
            scanner.close();
            throw new IllegalArgumentException("Source required for reading figures");
        }
        String nextWord = scanner.next();
        nextWord = nextWord.strip().toLowerCase();
        switch (nextWord) {
            case "random":
                return new RandomFigureFactory();
            case "systemin":
                System.out.println("Input number of figures to create:");
                return new StreamFigureFactory(new InputStreamReader(System.in));
            case "file":
                if (!scanner.hasNext()) {
                    throw new IllegalArgumentException("<filename> required for reading figures from file");
                }
                File file = new File(scanner.next());
                Reader fileReader = new FileReader(file);
                return new StreamFigureFactory(fileReader);
            default:
                scanner.close();
                throw new InvalidCommand("No creation method was recognized");
        }
    }
}
