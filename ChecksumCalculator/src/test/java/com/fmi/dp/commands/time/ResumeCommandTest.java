package com.fmi.dp.commands.time;

import com.fmi.dp.commands.calculate.ModeCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResumeCommandTest {
    private static TimeCommand timeCommand;

    private static final ModeCommand modeCommand1 = mock(ModeCommand.class);
    private static final ModeCommand modeCommand2 = mock(ModeCommand.class);

    @BeforeAll
    static void setUpBeforeClass() {
        when(modeCommand1.hasFinished()).thenReturn(false);
        when(modeCommand2.hasFinished()).thenReturn(true);

        doNothing().when(modeCommand1).resumeProcessing();
        doNothing().when(modeCommand2).resumeProcessing();
        timeCommand = new ResumeCommand(new CopyOnWriteArraySet<>(Set.of(modeCommand1, modeCommand2)));
    }

    @Test
    void testPauseRightCallPauseOrRemoveFromSet() {
        timeCommand.execute();
        verify(modeCommand1, times(1)).resumeProcessing();
        verify(modeCommand2, times(0)).resumeProcessing();
        assertEquals(1, timeCommand.commands().size());
    }
}
