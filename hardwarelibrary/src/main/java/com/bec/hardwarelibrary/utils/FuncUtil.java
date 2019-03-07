package com.bec.hardwarelibrary.utils;

public class FuncUtil {

    public FuncUtil() {
    }

    public static int isOdd(int num) {
        return num & 1;
    }

    public static int hexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static String byte2Hex(Byte inByte) {
        return String.format("%02x", inByte).toUpperCase();
    }

    public static String byteArrToHex(byte[] inBytArr) {

        StringBuilder strBuilder = new StringBuilder();

        for (byte anInBytArr : inBytArr) {
            strBuilder.append(byte2Hex(anInBytArr));
            strBuilder.append("");
        }

        return strBuilder.toString();
    }

    public static String byteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();

        for (int i = offset; i < byteCount; ++i) {
            strBuilder.append(byte2Hex(inBytArr[i]));
        }

        return strBuilder.toString();
    }

    public static byte[] hexToByteArr(String inHex) {
        int hexLen = inHex.length();
        byte[] result;
        if (isOdd(hexLen) == 1) {
            ++hexLen;
            result = new byte[hexLen / 2];
            inHex = "0" + inHex;
        } else {
            result = new byte[hexLen / 2];
        }

        int j = 0;

        for (int i = 0; i < hexLen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            ++j;
        }

        return result;
    }
}