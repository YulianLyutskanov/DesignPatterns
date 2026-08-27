package com.fmi.dp.observer;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseObservable implements ObservableApi {
    @NotNull
    private final Set<@NotNull ObserverApi> observers = new HashSet<>();

    @Override
    public void attach(@NotNull ObserverApi observer) {
        observers.add(observer);
    }

    @Override
    public void detach(@NotNull ObserverApi observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(@NotNull ObservableApi sender, @NotNull FileMessage message) {
        for (ObserverApi observer : observers) {
            observer.update(sender, message);
        }
    }
}
