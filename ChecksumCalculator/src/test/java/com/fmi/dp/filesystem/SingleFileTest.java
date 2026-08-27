package com.fmi.dp.filesystem;

import com.fmi.dp.visitor.FileSystemEntityVisitor;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.mockito.Mockito.*;

public class SingleFileTest {
    private static final SingleFile singleFile = new SingleFile(Path.of("some"), 13);

    @Test
    void testValidAcceptVisitor() {
        FileSystemEntityVisitor mockedVisitor = mock(FileSystemEntityVisitor.class);
        doNothing().when(mockedVisitor).visit(singleFile);

        singleFile.accept(mockedVisitor);
        verify(mockedVisitor, times(1)).visit(singleFile);
    }
}
