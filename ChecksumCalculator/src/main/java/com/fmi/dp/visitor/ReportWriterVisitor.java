package com.fmi.dp.visitor;

import com.fmi.dp.writer.BaseStreamWriter;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

public class ReportWriterVisitor extends BaseStreamWriter implements FileSystemEntityVisitor {
    public ReportWriterVisitor(@NotNull OutputStream outputStream) {
        super(outputStream);
    }

    private void visitWrite(@NotNull FileSystemEntity file) {
        try {
            streamWriter.write(String.format("We will file visit: %s - %db" + System.lineSeparator(),
                    file.getAbsolutePath(), file.getSize()));
            streamWriter.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Problem with writing in: " + streamWriter +
                    " while visiting in " + ReportWriterVisitor.class, e);
        }
    }

    @Override
    public void visit(@NotNull SingleFile file) {
        visitWrite(file);
    }

    @Override
    public void visit(@NotNull Directory directory) {
        visitWrite(directory);

        for (FileSystemEntity child : directory.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(@NotNull Shortcut shortcut) {
        visitWrite(shortcut);

        if (shortcut.getTarget() != null) {
            shortcut.getTarget().accept(this);
        }
    }
}
