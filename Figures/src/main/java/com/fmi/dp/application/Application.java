package com.fmi.dp.application;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;

import com.fmi.dp.exceptions.InvalidCommand;
import com.fmi.dp.factory.AbstractFactory;
import com.fmi.dp.collection.FigureCollection;
import com.fmi.dp.factory.FigureFactory;

public class Application {

    private FigureFactory factory = null;
    private FigureCollection figures = null;

    private void welcomingMessage() {
        System.out.println("======================================");
        System.out.println("|| Welcome to the Figure Workshop!  ||");
        System.out.println("======================================");
        System.out.println("Unleash your inner creator -> pick a mode to begin:");
    }

    private void writeCreatingOptionMessage() {
        System.out.println("1. From input: SystemIN");
        System.out.println("2. From file: File <filename>");
        System.out.println("3. Random Figures: Random");
        exitMessage();
    }

    private void exitMessage() {
        System.out.println("To exit program: exit");
    }

    private void operationsMessage() {
        System.out.println("======================================");
        System.out.println("||          Available Commands      ||");
        System.out.println("======================================");
        System.out.println("1. SystemOUT               -> Display all figures");
        System.out.println("2. Delete <index>          -> Remove a figure");
        System.out.println("3. Copy <index>            -> Duplicate a figure");
        System.out.println("4. File <filename>         -> Save list to file");
        System.out.println("5. Perimeter <index>       -> Show figure perimeter");
        exitMessage();
        restartMessage();
    }

    private void restartMessage() {
        System.out.println("To restart program: restart");
    }

    private void validateExit(String exit) {
        if (exit.strip().equalsIgnoreCase("exit")) {
            System.exit(0);
        }
    }

    void fillInitialCollection() {
        figures = new FigureCollection();

        int count = factory.countFigures();
        for (int i = 0; i < count; ++i) {
            figures.add(factory.create());
        }
    }

    public void run() {
        welcomingMessage();
        Scanner in = new Scanner(System.in);

        while (factory == null) {
            try {
                writeCreatingOptionMessage();
                String option = in.nextLine();
                validateExit(option);
                factory = AbstractFactory.createFactory(option);
                fillInitialCollection();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                factory = null;
                continue;
            }
            menuForCommands();
            factory = null;
        }
    }

    private void execute(String command) {
        command = command.strip().toLowerCase();
        Scanner scanner = new Scanner(command);
        try {
            String input = scanner.next();
            switch (input) {
                case "systemout" -> figures.serialize(new PrintWriter(System.out, true));
                case "delete" -> {
                    int idxDelete = Integer.parseInt(scanner.next());
                    figures.delete(idxDelete);
                }
                case "copy" -> {
                    int idxCopy = Integer.parseInt(scanner.next());
                    figures.copy(idxCopy);
                }
                case "file" -> {
                    input = scanner.next();
                    try (Writer writer = new FileWriter(input)) {
                        figures.serialize(writer);
                    }
                }
                case "perimeter" -> {
                    int idxPer = Integer.parseInt(scanner.next());
                    System.out.println(figures.perimeter(idxPer));
                }
                default -> throw new IllegalArgumentException("Invalid command");
            }
        } catch (Exception e) {
            throw new InvalidCommand(e.getMessage(), e.getCause());
        }
    }

    private void menuForCommands() {
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                operationsMessage();
                String option = in.nextLine();
                validateExit(option);
                if (validateRestart(option)) {
                    break;
                }
                execute(option);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean validateRestart(String exit) {
        return exit.strip().equalsIgnoreCase("restart");
    }

}
