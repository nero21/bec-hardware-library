package com.bec.hardwarelibrary.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lzp on 2018/6/25 0025.
 */
@SuppressWarnings("WeakerAccess")
public class Utils {

    //校验通过
    public static final int RIGHT_STATUS = 0;

    //校验不通过
    public static final int WRONG_STATUS = 1;

    //数据长度有误
    public static final int ILLEGAL_DATA = 2;

    //读数小于0
    public static final int BELOW_ZERO = 3;

    /**
     * @param buffer 电子秤原数据
     * @return 是否通过校验
     */
    public static int checkLegal(byte[] buffer) {

        if (buffer.length != 16) return ILLEGAL_DATA;

        if (((int) buffer[3]) != 32) return BELOW_ZERO;

        byte[] temp = Arrays.copyOfRange(buffer, 2, 12);

        byte start = temp[0];

        for (int i = 1; i < temp.length; i++) {
            start = (byte) (start ^ temp[i]);
        }

        if (start == buffer[12]) {
            return RIGHT_STATUS;
        }

        return WRONG_STATUS;
    }

    /**
     * 去除byte[]内的0
     *
     * @param buffer:byte[]
     * @return 去除0的有效数据
     */
    public static byte[] getVirtualValue(byte[] buffer) {

        int zeroNumber = 0;

        for (byte aBuffer : buffer) {
            if (aBuffer == (byte) 0) {
                zeroNumber++;
            }
        }

        byte[] newByte = new byte[buffer.length - zeroNumber];

        int i = 0;

        for (byte aBuffer : buffer) {
            if (aBuffer != (byte) 0) {
                newByte[i] = aBuffer;
                i++;
            }
        }

        return newByte;
    }

    /**
     * @param byte1:byte[]
     * @param byte2:byte[]
     * @return byte1 与 byte2拼接的结果
     */
    public static byte[] addBytes(byte[] byte1, byte[] byte2) {
        byte[] data3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, data3, 0, byte1.length);
        System.arraycopy(byte2, 0, data3, byte1.length, byte2.length);
        return data3;

    }

}
