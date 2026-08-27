package com.fmi.dp.checksumcalculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChecksumCalculatorTest {
    @Test
    void testCreateChecksumCalculatorMD5() {
        String md = "mD5";
        assertEquals(MD5ChecksumCalculator.class, ChecksumCalculator.create(md).getClass(),
                "After requesting for: " + md + " should return MD5ChecksumCalculator");
    }

    @Test
    void testCreateChecksumCalculatorSHA256() {
        String sha = "SHa256";
        assertEquals(SHA256ChecksumCalculator.class, ChecksumCalculator.create(sha).getClass(),
                "After requesting for: " + sha + " should return Sha256ChecksumCalculator");
    }

    @Test
    void testCreateChecksumCalculatorOther() {
        assertThrows(IllegalArgumentException.class, () -> ChecksumCalculator.create("unknown"),
                "After requesting for: " + null + " should throw IllegalArgumentException");
    }
}
