package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ShortcutTest {
    private static final FileSystemEntity target = mock();

    private static final Shortcut SHORTCUT = new Shortcut(Path.of("some"), 13);

    @BeforeAll
    static void setUp() {
        when(target.getSize()).thenReturn(20L);

        SHORTCUT.setTarget(target);
    }

    @Test
    void testSizeDirectory() {
        assertEquals(13L + 20L, SHORTCUT.getSize());
    }

    @Test
    void testValidAcceptVisitor() {
        FileSystemEntityVisitor mockedVisitor = mock(FileSystemEntityVisitor.class);
        doNothing().when(mockedVisitor).visit(SHORTCUT);

        SHORTCUT.accept(mockedVisitor);
        verify(mockedVisitor, times(1)).visit(SHORTCUT);
    }

    @Test
    void testSizeWithoutTarget() {
        Shortcut shortcutCurr = new Shortcut(Path.of("some"), 23);
        assertEquals(23L, shortcutCurr.getSize());
    }

}
