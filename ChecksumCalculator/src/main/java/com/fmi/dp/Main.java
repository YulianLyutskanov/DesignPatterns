package com.fmi.dp;

import com.fmi.dp.commands.Command;
import com.fmi.dp.commands.CommandParser;
import com.fmi.dp.commands.manager.TaskMementoVisitorManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        generateOneMBFile();

        TaskMementoVisitorManager visitorManager = new TaskMementoVisitorManager();
        while (true) {
            String s;
            Scanner scanner = new Scanner(System.in);
            s = scanner.nextLine();
            String[] tokens = s.split(" ");
            try {
                Command c = CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(tokens)));
                visitorManager.visit(c);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void generateOneMBFile() throws IOException {
        Path folderPath = Paths.get("HashTestFolder");
        if (Files.notExists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        byte[] bytes = new byte[1024 * 1024];
        new Random().nextBytes(bytes);
        Files.write(folderPath.resolve("1MB.txt"), bytes);
    }
}
