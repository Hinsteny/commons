package com.github.hinsteny.commons.core.utils;

/**
 * 字节操作工具
 *
 * @author Hinsteny
 * @version ByteUtil: ByteUtil 2019-05-08 17:36 All rights reserved.$
 */
public class ByteUtil {

    /**
     * translate bytes arrays to hex-string
     *
     * @param bytes
     * @return
     */
    public static String byteToStr(byte[] bytes) {
        final StringBuilder sbr = new StringBuilder();
        if (null != bytes && bytes.length > 0) {
            for (byte item : bytes) {
                sbr.append(",").append(item);
            }
        }
        return sbr.length() > 0 ? sbr.substring(1) : sbr.toString();
    }

    /**
     * translate bytes arrays to hex-string
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        return byteToHex(bytes, false);
    }

    /**
     * translate bytes arrays to hex-string and to uppercase
     *
     * @param bytes
     * @param toUpperCase
     * @return
     */
    public static String byteToHex(byte[] bytes, boolean toUpperCase) {
        StringBuilder hex = new StringBuilder();
        String temp;
        for (byte i : bytes) {
            temp = Integer.toHexString(0x00ff & i);
            if (temp.length() == 1) {
                temp = "0".concat(temp);
            }
            hex.append(temp);
        }
        String hexStr = hex.toString();
        return toUpperCase ? hexStr.toUpperCase() : hexStr;
    }

    /**
     * translate hex-string to bytes
     *
     * @param source
     * @return
     */
    public static byte[] hexTBytes(String source) {
        if (null == source || source.length() % 2 != 0) {
            throw new IllegalArgumentException("Illegal parameters");
        }
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }

    /**
     * translate hex-bytes to bytes
     *
     * @param data
     * @return
     */
    public static byte[] hexTBytes(byte[] data) {
        if (null == data || data.length % 2 != 0) {
            throw new IllegalArgumentException("Illegal parameters");
        }
        byte[] bytes = new byte[data.length / 2];
        String temp;
        for (int n = 0; n < data.length; n += 2) {
            temp = new String(data, n, 2);
            bytes[n / 2] = (byte) Integer.parseInt(temp, 16);
        }

        return bytes;
    }

}
