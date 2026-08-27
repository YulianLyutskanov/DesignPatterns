package com.fmi.dp.reporter;

public class Stopwatch {
    private long startTime;
    private long elapsedTime;
    private boolean running;

    public Stopwatch() {
        reset();
    }

    public void start() {
        if (!running) {
            startTime = System.currentTimeMillis() - elapsedTime;
            running = true;
        }
    }

    public void stop() {
        if (running) {
            elapsedTime = System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    public void reset() {
        elapsedTime = 0;
        startTime = 0;
        running = false;
    }

    public long elapsedMilliseconds() {
        return running ? System.currentTimeMillis() - startTime : elapsedTime;
    }

    public boolean isRunning() {
        return running;
    }
}
