package com.fmi.dp.commands.manager;

import com.fmi.dp.commands.Command;
import com.fmi.dp.commands.calculate.ModeCommand;
import com.fmi.dp.commands.time.ResumeCommand;
import com.fmi.dp.commands.time.TimeCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskMementoVisitorManagerTest {
    private static ModeCommand modeCommand;
    private static TimeCommand timeCommand;
    private TaskMementoVisitorManager taskMementoVisitorManager;

    @BeforeAll
    static void setUp() {
        modeCommand = mock();
        doNothing().when(modeCommand).execute();
        doNothing().when(modeCommand).resumeProcessing();
        timeCommand = new ResumeCommand();
    }

    @BeforeEach
    void setUpBefore() {
        taskMementoVisitorManager = new TaskMementoVisitorManager();
    }

    @Test
    void testAddToCommandOneTimeShouldReturnSizeOne() throws InterruptedException {
        taskMementoVisitorManager.visit(modeCommand);
        Thread.sleep(1);
        assertEquals(1, taskMementoVisitorManager.commands().size());

        when(modeCommand.hasFinished()).thenReturn(false);
        taskMementoVisitorManager.visit(timeCommand).join();
        assertEquals(1, taskMementoVisitorManager.commands().size());

        when(modeCommand.hasFinished()).thenReturn(true);
        taskMementoVisitorManager.visit(timeCommand).join();
        assertEquals(0, taskMementoVisitorManager.commands().size());
    }

    @Test
    void testFactoryVisitTimeCommand() {
        Command time = mock(TimeCommand.class);
        assertEquals(TimeCommand.class, taskMementoVisitorManager.visit(time));
    }

    @Test
    void testFactoryVisitModeCommand() {
        Command time = mock(ModeCommand.class);
        assertEquals(ModeCommand.class, taskMementoVisitorManager.visit(time));
    }

    @Test
    void testFactoryInvalidCommand() {
        class Invalid implements Command {
            @Override
            public void execute() {

            }
        }
        Command invalid = mock(Invalid.class);
        assertThrows(IllegalArgumentException.class, () -> taskMementoVisitorManager.visit(invalid));
    }
}
