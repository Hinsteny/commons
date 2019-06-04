package com.github.hinsteny.commons.core.functional.secret;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64 编解码工具类
 *
 * @author Hinsteny
 * @version Base64Util: Base64Util 2019-05-08 19:27 All rights reserved.$
 */
public class Base64Util {

    /**
     * 默认的字符编码
     */
    private static String default_charset = "8859_1";

    /**
     * 进行base64加码
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Encodes(String data) throws UnsupportedEncodingException {
        String charset = default_charset;
        byte[] srcs = data.getBytes(charset);
        byte[] dst = base64Encode(srcs);
        return new String(dst, charset);
    }

    /**
     * 进行base64加码
     * @param data
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Encodes(String data, String charset) throws UnsupportedEncodingException {
        byte[] srcs = data.getBytes(charset);
        byte[] dst = base64Encode(srcs);
        return new String(dst, charset);
    }

    /**
     * 进行base64加码
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Encodes(byte[] data) {
        byte[] dst = base64Encode(data);
        return new String(dst);
    }

    /**
     * 进行base64加码
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Encodes(byte[] data, String charset) throws UnsupportedEncodingException {
        byte[] dst = base64Encode(data);
        return new String(dst, charset);
    }

    /**
     * 进行base64解码
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Decodes(String data) throws UnsupportedEncodingException {
        String charset = default_charset;
        byte[] srcs = data.getBytes(charset);
        byte[] dst = base64Decode(srcs);
        return new String(dst, charset);
    }

    /**
     * 进行base64解码
     * @param data
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Decode(String data, String charset) throws UnsupportedEncodingException {
        byte[] srcs = data.getBytes(charset);
        byte[] dst = base64Decode(srcs);
        return new String(dst, charset);
    }

    /**
     * 进行base64解码
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Decodes(byte[] data) throws UnsupportedEncodingException {
        byte[] dst = base64Decode(data);
        return new String(dst);
    }

    /**
     * 进行base64解码
     * @param data
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String base64Decodes(byte[] data, String charset) throws UnsupportedEncodingException {
        byte[] dst = base64Decode(data);
        return new String(dst, charset);
    }

    /**
     * 对字节数组进行base64加码
     * @param data
     * @return
     */
    public static byte[] base64Encode(byte[] data) {
        byte[] dst = Base64.getEncoder().encode(data);
        return dst;
    }

    /**
     * 对字节数组进行base64解码
     * @param data
     * @return
     */
    public static byte[] base64Decode(byte[] data) {
        byte[] dst = Base64.getDecoder().decode(data);
        return dst;
    }

}
