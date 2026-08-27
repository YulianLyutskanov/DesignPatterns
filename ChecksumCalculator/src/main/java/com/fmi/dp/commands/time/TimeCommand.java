package com.fmi.dp.commands.time;

import com.fmi.dp.commands.calculate.ModeCommand;
import com.fmi.dp.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class TimeCommand implements Command {
    protected Set<ModeCommand> commands;

    public TimeCommand(@NotNull Set<@NotNull ModeCommand> commands) {
        this.commands = commands;
    }

    public TimeCommand() {
        this.commands = new CopyOnWriteArraySet<>();
    }

    public void setCommands(@NotNull Set<@NotNull ModeCommand> commands) {
        this.commands = commands;
    }

    public Set<@NotNull ModeCommand> commands() {
        return commands;
    }
}
