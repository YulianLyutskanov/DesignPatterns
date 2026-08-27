package com.fmi.dp.commands.time;

import com.fmi.dp.commands.calculate.ModeCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PauseCommand extends TimeCommand {
    public PauseCommand(@NotNull Set<@NotNull ModeCommand> commands) {
        super(commands);
    }

    public PauseCommand() {
        super();
    }

    @Override
    public void execute() {
        for (ModeCommand currentCalculate : commands) {
            if (!currentCalculate.hasFinished()) {
                currentCalculate.pauseProcessing();
            } else {
                commands.remove(currentCalculate);
            }
        }
    }
}
