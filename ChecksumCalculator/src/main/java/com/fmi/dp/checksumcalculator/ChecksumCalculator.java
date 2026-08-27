package com.fmi.dp.checksumcalculator;

import com.fmi.dp.observer.ObservableApi;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface ChecksumCalculator extends ObservableApi {
    String calculate(@NotNull InputStream stream);

    static ChecksumCalculator create(@NotNull String type) {
        return switch (type.toLowerCase()) {
            case "md5" -> new MD5ChecksumCalculator();
            case "sha256" -> new SHA256ChecksumCalculator();
            default -> throw new IllegalArgumentException("Invalid checksum type: " + type);
        };
    }
}
