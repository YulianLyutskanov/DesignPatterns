package com.fmi.dp.checksumcalculator;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MD5ChecksumCalculatorTest {
    private static final String MD = "MD5";

    @Test
    public void testMD5ChecksumCalculatorReturnsRightChecksumCalculator() throws NoSuchAlgorithmException {
        // Get the expected MessageDigest instance
        MessageDigest expectedDigest = MessageDigest.getInstance(MD);

        // Get the actual MessageDigest from the MD5ChecksumCalculator
        MessageDigest actualDigest = new MD5ChecksumCalculator().getHashAlgorithm();

        // Assert that the algorithm name is the same (MD5)
        assertEquals(expectedDigest.getAlgorithm(), actualDigest.getAlgorithm(),
                "Expected MD5 algorithm but found: " + actualDigest.getAlgorithm());
    }

}
