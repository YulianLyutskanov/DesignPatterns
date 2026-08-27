package com.fmi.dp.reporter;

import com.fmi.dp.observer.ObserverApi;

public interface ProgressReporterApi extends ObserverApi {
    void startTimer(long expectedTotalBytes);

    long endTimer();
}
