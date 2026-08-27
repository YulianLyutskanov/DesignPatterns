package com.fmi.dp.commands;

import com.fmi.dp.commands.calculate.CalculateCommand;
import com.fmi.dp.commands.calculate.VerifyCommand;
import com.fmi.dp.commands.time.PauseCommand;
import com.fmi.dp.commands.time.ResumeCommand;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandParserTest {
    @Test
    void testValidCalculateCommandFactory() {
        String[] args = {"--mode=calculate"};
        Command c = CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(args)));
        assertEquals(CalculateCommand.class, c.getClass());
    }

    @Test
    void testValidVerifyCommandFactory() {
        String[] args = {"--mode=verify"};
        Command c = CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(args)));
        assertEquals(VerifyCommand.class, c.getClass());
    }

    @Test
    void testValidPauseCommandFactory() {
        String[] args = {"--mode=pause"};
        Command c = CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(args)));
        assertEquals(PauseCommand.class, c.getClass());
    }

    @Test
    void testValidResumeCommandFactory() {
        String[] args = {"--mode=resume"};
        Command c = CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(args)));
        assertEquals(ResumeCommand.class, c.getClass());
    }

    @Test
    void testInvalidCommandFactory() {
        String[] args = {"--mode=invalid"};
        assertThrows(IllegalArgumentException.class,
                () -> CommandParser.createCommand(Objects.requireNonNull(CommandParser.parseArguments(args))));
    }
}
