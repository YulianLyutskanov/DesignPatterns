package com.fmi.dp.observer;

import org.jetbrains.annotations.NotNull;

/**
 * @param size long is used for large numbers
 */
public record FileMessage(@NotNull String fileName, long size, boolean alreadyProcessed) {
}
