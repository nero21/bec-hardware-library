package com.bec.hardwarelibrary.utils;

public class FuncUtil {

    public FuncUtil() {
    }

    public static int isOdd(int num) {
        return num & 1;
    }

    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", inByte).toUpperCase();
    }

    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;

        for (byte anInBytArr : inBytArr) {
            strBuilder.append(Byte2Hex(anInBytArr));
            strBuilder.append("");
        }

        return strBuilder.toString();
    }

    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();

        for (int i = offset; i < byteCount; ++i) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }

        return strBuilder.toString();
    }

    public static byte[] HexToByteArr(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {
            ++hexlen;
            result = new byte[hexlen / 2];
            inHex = "0" + inHex;
        } else {
            result = new byte[hexlen / 2];
        }

        int j = 0;

        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            ++j;
        }

        return result;
    }
}