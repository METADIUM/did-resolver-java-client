package com.metaidum.did.resolver.client.util;

import java.math.BigInteger;

public class Hex {
    public static byte[] hexStringToByteArray(String input) {
        int len = input.length();

        if (len == 0) {
            return new byte[] {};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(input.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }
        return data;
    }
    
    public static String toHexString(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            stringBuilder.append(String.format("%02x", input[i] & 0xFF));
        }
        return stringBuilder.toString();
    }
    
    public static String toHexStringZeroPadding(BigInteger num, int size) {
    	StringBuilder s = new StringBuilder(num.toString(16));
    	
    	while (s.length() < size) {
    		s.insert(0, '0');
    	}
    	return s.toString();
    }
}
