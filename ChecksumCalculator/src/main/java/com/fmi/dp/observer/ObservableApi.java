package com.fmi.dp.observer;

import org.jetbrains.annotations.NotNull;

public interface ObservableApi {
    void attach(@NotNull ObserverApi observer);

    void detach(@NotNull ObserverApi observer);

    void notify(@NotNull ObservableApi sender, @NotNull FileMessage message);
}
