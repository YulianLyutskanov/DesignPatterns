package com.fmi.dp.checksumcalculator;

import com.fmi.dp.exceptions.HashAlgorithmNotFound;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256ChecksumCalculator extends BaseChecksumCalculator {

    @Override
    protected MessageDigest getHashAlgorithm() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HashAlgorithmNotFound("SHA-256 algorithm not found", e);
        }
    }
}
