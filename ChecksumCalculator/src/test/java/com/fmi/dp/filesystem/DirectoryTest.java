package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DirectoryTest {
    private static final FileSystemEntity entity1 = mock();
    private static final FileSystemEntity entity2 = mock();

    private static final Directory DIR = new Directory(Path.of("Some"));

    @BeforeAll
    static void setUp() {
        when(entity1.getSize()).thenReturn(50L);
        when(entity2.getSize()).thenReturn(24L);

        DIR.addChild(entity1);
        DIR.addChild(entity2);
    }

    @Test
    void testSizeDirectory() {
        assertEquals(50L + 24L, DIR.getSize());
    }

    @Test
    void testGetChildrenInvalidChangeOutside() {
        assertThrows(UnsupportedOperationException.class, () -> DIR.getChildren().clear());
    }

    @Test
    void testValidAcceptVisitor() {
        FileSystemEntityVisitor mockedVisitor = mock(FileSystemEntityVisitor.class);
        doNothing().when(mockedVisitor).visit(DIR);

        DIR.accept(mockedVisitor);
        verify(mockedVisitor, times(1)).visit(DIR);
    }
}
