package com.fmi.dp.commands.manager;

import com.fmi.dp.commands.Command;
import com.fmi.dp.commands.calculate.ModeCommand;
import com.fmi.dp.commands.time.TimeCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2 threads working for 1 client, 1 for stop/resume and 1 for calculating operations with Percentage logic
 * But that does not mean we can't pause one calculation and add another in the set and when we stop/resume,
 * to stop/resume more than one, they will just wait themselves, not parallel because
 * there is again logic with Percentage Progress
 */
public class TaskMementoVisitorManager {
    @NotNull
    private final Set<@NotNull ModeCommand> commands = new CopyOnWriteArraySet<>();
    //only 1 thread for 1 client because there is printing logic with %
    //so we don't want to mix % from different calculations
    @NotNull
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void visit(@NotNull ModeCommand command) {
        executor.execute(() -> {
            commands.add(command);
            command.execute();
        });
    }

    public Thread visit(@NotNull TimeCommand command) {
        //lite operation and rarely used so no need for platform thread
        return Thread.ofVirtual().start(() -> {
            command.setCommands(commands);
            command.execute();
        });
    }

    public Class<? extends Command> visit(@NotNull Command command) {
        return switch (command) {
            case ModeCommand modeCommand -> {
                visit(modeCommand);
                yield ModeCommand.class;
            }
            case TimeCommand timeCommand -> {
                visit(timeCommand);
                yield TimeCommand.class;
            }
            default -> throw new IllegalArgumentException("Unsupported command type: " +
                            command.getClass().getSimpleName());
        };
    }

    public Set<ModeCommand> commands() {
        return Collections.unmodifiableSet(commands);
    }
}
