package com.github.hinsteny.commons.core.functional.secret;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加解密算法工具 加密用的Key 可以用若干个字母和数字组成，最好不要用保留字符 DES: DES-56-CBC加密模式，key需要为8位。 3DES: DESede-168-CBC加密模式，key需要为24位。
 *
 * @author Hinsteny
 * @version DESUtil: DESUtil 2019-05-10 10:13 All rights reserved.$
 */
public class DESUtil {

    /**
     * 编码类型
     */
    private static final String CHARCODE = "UTF-8";

    /**
     * 默认的DES秘钥长度
     **/
    private static final int DEFAULT_DES_KEY_LENGTH = 56;

    /**
     * 默认的3DES秘钥长度
     **/
    private static final int DEFAULT_3DES_KEY_LENGTH = 168;

    /**
     * 使用CBC模式，需要一个向量iv，可增加加密算法的强度
     */
    private static final String MODEL = "12345678";

    /**
     * 算法
     */
    private static final String[] algorithms = {"DES", "DESede"};

    /**
     * [DES] 算法/模式/补码方式
     */
    private static final String ALGORITHMDES = "DES/CBC/PKCS5Padding";

    /**
     * [3DES] 算法/模式/补码方式
     */
    private static final String ALGORITHM3DES = "DESede/CBC/PKCS5Padding";

    private static final Map<String, String> ALGORITHMS_MAP;

    static {
        Map<String, String> data = new HashMap<>();
        data.put(algorithms[0], ALGORITHMDES);
        data.put(algorithms[1], ALGORITHM3DES);

        ALGORITHMS_MAP = Collections.unmodifiableMap(data);
    }

    /**
     * 生成一个DES加密私钥串
     *
     * @return 秘钥ken
     * @throws Exception 异常
     */
    public static String generateDESKey() throws Exception {
        return generateDESKey(algorithms[0], DEFAULT_DES_KEY_LENGTH);
    }

    /**
     * 生成一个DES加密私钥串
     *
     * @return 秘钥ken
     * @throws Exception 异常
     */
    public static String generate3DESKey() throws Exception {
        return generateDESKey(algorithms[1], DEFAULT_3DES_KEY_LENGTH);
    }

    /**
     * 生成一个DES加密私钥串
     *
     * @param keyLen AES秘钥长度可选值有
     * @return 秘钥ken
     * @throws Exception 异常
     */
    private static String generateDESKey(String algorithm, int keyLen) throws Exception {
        if (!(DEFAULT_DES_KEY_LENGTH == keyLen || DEFAULT_3DES_KEY_LENGTH == keyLen)) {
            throw new Exception("DES key length is not correct");
        }
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kg.init(keyLen);
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        return byteToHexString(b);
    }

    /**
     * 判断key长度有效性
     *
     * @param key 秘钥
     * @param needKeyLen 秘钥长度
     * @return result
     */
    private static boolean judgeKey(String key, int needKeyLen) throws Exception {
        if (key == null || "".equals(key.trim())) {
            throw new Exception("DES key is not valid");
        }
        if (needKeyLen != key.length()) {
            throw new Exception("DES key length is not correct");
        }
        return true;
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes 字节
     * @return 转化后的内容
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int source = (bytes[i] & 0xFF);
            String strHex = Integer.toHexString(source);
            sb.append(strHex.charAt(0));
        }
        return sb.toString();
    }

    /**
     * 加密
     *
     * @param src 需要加密的内容
     * @param key 加密秘钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encrypt(String src, String key) throws Exception {
        judgeKey(key, 8);
        return encrypt(src, key, algorithms[0], CHARCODE);
    }

    /**
     * 加密
     *
     * @param src 需要加密的内容
     * @param key 加密秘钥
     * @return 加密结果
     * @throws Exception 异常
     */
    public static String encrypt3DES(String src, String key) throws Exception {
        judgeKey(key, 24);
        return encrypt(src, key, algorithms[1], CHARCODE);
    }

    /**
     * 解密
     *
     * @param src 密文
     * @param key 密钥
     * @return 解密结果
     * @throws Exception 异常
     */
    public static String decrypt(String src, String key) throws Exception {
        judgeKey(key, 8);
        return decrypt(src, key, algorithms[0], CHARCODE);
    }

    /**
     * 解密
     *
     * @param src 密文
     * @param key 密钥
     * @return 解密结果
     * @throws Exception 异常
     */
    public static String decrypt3DES(String src, String key) throws Exception {
        judgeKey(key, 24);
        return decrypt(src, key, algorithms[1], CHARCODE);
    }

    /**
     * 加密
     *
     * @param src 需要加密的内容
     * @param key 加密秘钥
     * @param algorithm 加密所用算法
     * @param charset 编码
     * @return 加密结果
     * @throws Exception 异常
     */
    private static String encrypt(String src, String key, String algorithm, String charset) throws Exception {
        String afterCode;
        byte[] raw = key.getBytes();
        Cipher cipher = Cipher.getInstance(ALGORITHMS_MAP.get(algorithm));
        SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
        IvParameterSpec iv = new IvParameterSpec(MODEL.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(src.getBytes(charset));
        //此处使用BASE64做转码功能，防止中文乱码
        afterCode = Base64Util.base64Encodes(encrypted);
        return afterCode.replaceAll("\r|\n", "");
    }

    /**
     * 解密
     *
     * @param src 需要解密的内容
     * @param key 解密秘钥
     * @param algorithm 解密所用算法
     * @param charset 编码
     * @return 解密结果
     * @throws Exception 异常
     */
    private static String decrypt(String src, String key, String algorithm, String charset) throws Exception {
        String originalString;
        byte[] raw = key.getBytes(CHARCODE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
        Cipher cipher = Cipher.getInstance(ALGORITHMS_MAP.get(algorithm));
        IvParameterSpec iv = new IvParameterSpec(MODEL.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        //先用base64解密
        byte[] secret = Base64Util.base64Decode(src.getBytes(charset));
        byte[] original = cipher.doFinal(secret);
        originalString = new String(original, charset);
        return originalString;
    }

}
