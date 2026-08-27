package com.fmi.dp.commands;

import com.fmi.dp.commands.calculate.CalculateCommand;
import com.fmi.dp.commands.calculate.VerifyCommand;
import com.fmi.dp.commands.time.PauseCommand;
import com.fmi.dp.commands.time.ResumeCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;

public class CommandParser {
    private static final Options OPTIONS = new Options();
    private static final CommandLineParser PARSER = new DefaultParser();
    private static final HelpFormatter FORMATTER = new HelpFormatter();

    static {
        addOption("m", "mode", true, "Operation mode: calculate or verify, pause or resume (default: calculate)", true);
        addOption("p", "path", true, "Target file or directory to check (default: current directory)", false);
        addOption("a", "algorithm", true, "Algorithms sha256/md5 (default: SHA256)", false);
        addOption("nfl", "not-follow-links", false,
                "Indicates whether to follow links (default: following links)", false);
        addOption("c", "checksums", true,
                "Path to checksum file (to read in verify mode or to write in calc mode)" +
                        " (default: currDir/checksums.txt)", false);
        addOption("t", "pathto", true, "Choose path to verify (default: client console)", false);
    }

    private static void addOption(@NotNull String opt,
                                  @NotNull String longOpt,
                                  boolean hasArg,
                                  @NotNull String description,
                                  boolean required) {
        Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(required);
        OPTIONS.addOption(option);
    }

    public static CommandLine parseArguments(@NotNull String[] args) {
        try {
            return PARSER.parse(OPTIONS, args);
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            FORMATTER.printHelp("checksum-calculator", OPTIONS);
        }
        return null;
    }

    public static Command createCommand(CommandLine cmd) {
        var mode = cmd.getOptionValue("mode", "calculate");

        return switch (mode.toLowerCase()) {
            case "verify" -> new VerifyCommand(cmd);
            case "calculate" -> new CalculateCommand(cmd);
            case "pause" -> new PauseCommand();
            case "resume" -> new ResumeCommand();
            default -> throw new IllegalArgumentException("Invalid mode: " + mode);
        };
    }
}
