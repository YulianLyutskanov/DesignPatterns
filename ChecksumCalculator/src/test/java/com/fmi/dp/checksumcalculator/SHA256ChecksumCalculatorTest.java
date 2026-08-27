package com.fmi.dp.checksumcalculator;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SHA256ChecksumCalculatorTest {
    private static final String SHA = "SHA-256";

    @Test
    public void testMD5ChecksumCalculatorReturnsRightChecksumCalculator() throws NoSuchAlgorithmException {
        // Get the expected MessageDigest instance
        MessageDigest expectedDigest = MessageDigest.getInstance(SHA);

        // Get the actual MessageDigest from the MD5ChecksumCalculator
        MessageDigest actualDigest = new SHA256ChecksumCalculator().getHashAlgorithm();

        // Assert that the algorithm name is the same (MD5)
        assertEquals(expectedDigest.getAlgorithm(), actualDigest.getAlgorithm(),
                "Expected SHA256 algorithm but found: " + actualDigest.getAlgorithm());
    }
}
