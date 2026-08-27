package com.fmi.dp.visitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.visitor.formateditors.FormatEditor;
import com.fmi.dp.visitor.memento.ProcessedFilesSnapshot;
import com.fmi.dp.writer.BaseObservableStreamWriter;
import com.fmi.dp.filesystem.FileSystemEntity;
import com.fmi.dp.filesystem.Directory;
import com.fmi.dp.filesystem.Shortcut;
import com.fmi.dp.filesystem.SingleFile;
import com.fmi.dp.observer.FileMessage;
import com.fmi.dp.observer.ObserverApi;
import com.fmi.dp.visitor.memento.FileSystemEntityMementoVisitor;
import org.jetbrains.annotations.NotNull;

public class HashStreamWriterVisitor extends BaseObservableStreamWriter implements FileSystemEntityMementoVisitor {
    @NotNull
    private Set<@NotNull Path> visitedEntities;
    private FileSystemEntity root = null;
    @NotNull
    private final ChecksumCalculator checksumCalculator;
    @NotNull
    private final FormatEditor formatEditor;

    @NotNull
    private final AtomicBoolean stopped = new AtomicBoolean(false);

    private boolean finished = false;

    public HashStreamWriterVisitor(
            @NotNull OutputStream outputStream,
            @NotNull ChecksumCalculator checksumCalculator,
            @NotNull FormatEditor editor) {
        super(outputStream);
        this.checksumCalculator = checksumCalculator;
        this.formatEditor = editor;
        visitedEntities = new HashSet<>();
    }

    private void finalizeOutputStream() {
        try {
            formatEditor.writeChecksumAtEnd(streamWriter, checksumCalculator);
        } catch (IOException e) {
            throw new UncheckedIOException("Invalid outputStream: " + streamWriter, e);
        }
    }

    private void startOutputStream() {
        try {
            formatEditor.writeChecksumAtBeginning(streamWriter, checksumCalculator);
        } catch (IOException e) {
            throw new UncheckedIOException("Invalid outputStream: " + streamWriter, e);
        }
    }

    @Override
    public void attach(@NotNull ObserverApi observer) {
        checksumCalculator.attach(observer);
        super.attach(observer);
    }

    private void visitSingleEntityFile(@NotNull FileSystemEntity file) {
        synchronized (stopped) {
            stopped.notifyAll();
            if (stopped.get()) {
                //if the snapshot has been taken first then we don't process this one, we delay it later
                return;
            }
            if (root == null) {
                startOutputStream();
            }
            if (visitedEntities.contains(file.getAbsolutePath())) {
                notify(this, new FileMessage(file.getAbsolutePath().toString(), file.getSize(), true));
            } else {
                notify(this, new FileMessage(file.getAbsolutePath().toString(), file.getSize(), false));

                try (InputStream stream = new FileInputStream(file.getAbsolutePath().toFile())) {
                    String checksum = checksumCalculator.calculate(stream);
                    formatEditor.writeChecksum(streamWriter, file, checksum);
                } catch (IOException ex) {
                    throw new UncheckedIOException("Error accessing file " +
                            ex.getMessage() + System.lineSeparator(), ex);
                }
                visitedEntities.add(file.getAbsolutePath());
            }
            if (root == null) {
                finished = true;
                finalizeOutputStream();
            }
        }
    }

    @Override
    public void visit(@NotNull SingleFile file) {
        visitSingleEntityFile(file);
    }

    @Override
    public void visit(@NotNull Shortcut file) {
        if (stopped.get()) {
            return;
        }
        if (root == null) {
            root = file;
            startOutputStream();
        }

        if (visitedEntities.contains(file.getAbsolutePath())) {
            notify(this, new FileMessage(file.getAbsolutePath().toString(), file.getSize(), true));
        } else {
            if (file.getTarget() != null) {
                file.getTarget().accept(this);
            }
            if (stopped.get()) {
                return;
            }
            visitSingleEntityFile(file);
            visitedEntities.add(file.getAbsolutePath());
        }
        if (root == file) {
            finished = true;
            finalizeOutputStream();
        }
    }

    @Override
    public void visit(@NotNull Directory directory) {
        if (stopped.get()) {
            return;
        }
        if (root == null) {
            startOutputStream();
            root = directory;
        }

        if (visitedEntities.contains(directory.getAbsolutePath())) {
            notify(this, new FileMessage(directory.getAbsolutePath().toString(), directory.getSize(), true));
        } else {
            notify(this, new FileMessage(directory.getAbsolutePath().toString(), directory.getSize(), false));

            for (FileSystemEntity file : directory.getChildren()) {
                if (stopped.get()) {
                    return;
                }
                file.accept(this);
            }
            if (stopped.get()) {
                return;
            }
            visitedEntities.add(directory.getAbsolutePath());
        }
        if (root == directory) {
            finished = true;
            finalizeOutputStream();
        }
    }

    @Override
    public boolean hasFinished() {
        return finished;
    }

    @Override
    public boolean isStopped() {
        return stopped.get();
    }

    @Override
    public void pause() {
        stopped.set(true);
    }

    @Override
    public ProcessedFilesSnapshot getSnapshot() {
        //waiting until the current SingleFileEntity is fully written and then get the snapshot
        synchronized (stopped) {
            stopped.notifyAll();
            return new ProcessedFilesSnapshot(visitedEntities, root);
        }
    }

    @Override
    public void restore(@NotNull ProcessedFilesSnapshot snapshot) {
        synchronized (stopped) {
            stopped.notifyAll();
            visitedEntities = snapshot.visited();
            root = snapshot.root();
            stopped.set(false);
        }
        root.accept(this);
    }
}
