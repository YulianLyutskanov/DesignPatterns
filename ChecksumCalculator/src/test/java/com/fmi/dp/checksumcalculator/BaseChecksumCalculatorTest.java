package com.fmi.dp.checksumcalculator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseChecksumCalculatorTest {
    private final byte[] array = {0x00, 0x0f, 0x10, (byte)0xff};

    public class DerivedTest extends BaseChecksumCalculator {
        @Override
        protected MessageDigest getHashAlgorithm() {
            MessageDigest mockMessageDigest = mock();
            when(mockMessageDigest.digest()).thenReturn(array);
            return mockMessageDigest;
        }
    }

    @Test
    public void testBaseChecksumCalculator() {
        //we don't want to test MessageDigest so mocked and testing only the logic with hex bytes appending in string
        BaseChecksumCalculator calculator = new DerivedTest();

        InputStream is = new ByteArrayInputStream("".getBytes());
        String result = "000f10ff";
        assertEquals(result, calculator.calculate(is),
                "when each byte is: " + Arrays.toString(array) +
                        " result string from calculate should be in hex: " + result);
    }
}
