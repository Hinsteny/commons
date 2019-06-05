package com.github.hinsteny.commons.core.functional.secret;

import com.github.hinsteny.commons.core.utils.AssertUtil;
import com.github.hinsteny.commons.core.utils.StringUtil;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.hinsteny.commons.core.utils.ByteUtil;

/**
 * AES加解密算法
 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符
 * 此处使用AES-128-CBC加密模式，key需要为16位。
 *
 * @author Hinsteny
 * @version AESUtil: AESUtil 2019-05-08 17:55 All rights reserved.$
 */
public class AESUtil {

    private static final Logger LOGGER = LogManager.getLogger(AESUtil.class);

    /** 编码类型 */
    private static final String CHARCODE = "UTF-8";

    /** 算法 */
    private static final String AES = "AES";

    /** AES秘钥长度可以是128, 192, 256, 默认为128 **/
    private static final int DEFAULT_KEY_LENGTH = 128;

    /** 默认的AES秘钥字符长度 **/
    private static final int DEFAULT_KEY_BYTE_LENGTH = 16;

    /** 算法/模式/补码方式 */
    private static final AlgorithmType DEFAULT_ALGORITHM = AlgorithmType.AES_CBC_NOPADDING;

    /** 使用CBC模式，可以加一个向量iv，以增加加密算法的强度 */
    private static final String MODEL = "0102030405060708";

    /**
     * 生成一个默认长度为128的AES key
     * @return 秘钥
     * @throws NoSuchAlgorithmException 异常
     */
    public static String generateAESKey() throws NoSuchAlgorithmException {
        return generateAESKey(DEFAULT_KEY_LENGTH);
    }

    /**
     * 指定秘钥长度, 生成AES key
     * @param keyLen AES秘钥长度可选值有[128, 192, 256]
     * @return 秘钥
     * @throws NoSuchAlgorithmException 异常
     */
    public static String generateAESKey(int keyLen) throws NoSuchAlgorithmException {
        byte[] keyByte = generateAESKeyByte(keyLen);
        return StringUtil.byteToHexString(keyByte);
    }

    /**
     *
     * @param keyLen AES秘钥长度可选值有
     * @return 秘钥
     * @throws NoSuchAlgorithmException 异常
     */
    public static byte[] generateAESKeyByte(int keyLen) throws NoSuchAlgorithmException {
        AssertUtil.assertTrue(128 == keyLen || 192 == keyLen || 256 == keyLen, "AES key length is not correct");
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(AES);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("NoSuchAlgorithmException", e);
            throw e;
        }
        kg.init(keyLen);
        SecretKey sk = kg.generateKey();
        byte[] keyByte = sk.getEncoded();
        return keyByte;
    }

    /**
     * AES加密
     *
     * @param content 需要加密的内容
     * @param key 加密秘钥
     * @return 加密后的内容
     * @throws Exception 异常
     */
    public static String encrypt(String content, String key) throws Exception {
        return encryptThenBase64(content, key, CHARCODE, DEFAULT_ALGORITHM);
    }

    /**
     * 对输入的字符串进行AES加密, 然后再做Base64转码,防止中文乱码
     *
     * @param content 需要加密的内容
     * @param key 加密秘钥
     * @param charset 编码
     * @param algorithm 加密算法
     * @return 加密后的内容
     * @throws Exception 异常
     */
    public static String encryptThenBase64(String content, String key, String charset, AlgorithmType algorithm) throws Exception {
        byte[] contentBytes = content.getBytes(charset);
        byte[] tBytes = encryptBytes(contentBytes, key.getBytes(), algorithm);
        String secret = Base64Util.base64Encodes(tBytes);
        return secret.replaceAll("\r|\n", "");
    }

    /**
     * 对输入的字符串进行AES加密, 然后再转为十六进制字符串
     *
     * @param content 需要加密的内容
     * @param key 加密秘钥
     * @param charset 编码
     * @param algorithm 加密算法
     * @return 加密后的内容
     * @throws Exception 异常
     */
    public static String encryptThenHex(String content, String key, String charset, AlgorithmType algorithm) throws Exception {
        byte[] contentBytes = content.getBytes(charset);
        byte[] tBytes = encryptBytes(contentBytes, key.getBytes(), algorithm);
        String secret = ByteUtil.byteToHex(tBytes);
        return secret.replaceAll("\r|\n", "");
    }

    /**
     * 对输入的字节数据进行AES加密
     *
     * @param content 需要加密的内容
     * @param key 加密秘钥
     * @param algorithm 加密算法
     * @return 加密后的内容
     * @throws Exception 异常
     */
    public static byte[] encryptBytes(byte[] content, byte[] key, AlgorithmType algorithm) throws Exception {
        AssertUtil.assertTrue(null != content && content.length != 0, "加密内容不能为空");
        AssertUtil.assertNotNull(algorithm, "加密算法不能为空");
        AssertUtil.assertTrue(null != key && key.length >= DEFAULT_KEY_BYTE_LENGTH, "秘钥长度至少为16位");
        Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
        SecretKeySpec skeySpec = new SecretKeySpec(key, AES);
        if (hasIv(algorithm)) {
            IvParameterSpec iv = new IvParameterSpec(MODEL.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        }
        byte[] encrypted = cipher.doFinal(content);
        return encrypted;
    }

    /**
     * AES解密
     *
     * @param content 密文
     * @param key 密钥
     * @return 解密后的内容
     * @throws Exception 异常
     */
    public static String decrypt(String content, String key) throws Exception {
        return decryptAfterBase64Decode(content, key, CHARCODE, DEFAULT_ALGORITHM);
    }

    /**
     * 先base64解密, 然后再AES解密
     *
     * @param content 密文
     * @param key 密钥
     * @param charset 编码
     * @param algorithm 加密算法
     * @return 解密后的内容
     * @throws Exception 异常
     */
    public static String decryptAfterBase64Decode(String content, String key, String charset, AlgorithmType algorithm) throws Exception {
        AssertUtil.assertTrue(null != content && content.length() != 0, "解密内容不能为空");
        AssertUtil.assertTrue(null != key && key.length() != 0, "秘钥不能为空");
        byte[] decodeBuffer = Base64.getDecoder().decode(content);
        byte[] decryptTBytes = decryptBytes(decodeBuffer, key.getBytes(), algorithm);
        String originalString = new String(decryptTBytes, charset);
        return originalString;
    }

    /**
     * 先做hex字节码还原, 然后再AES解密
     *
     * @param content 密文
     * @param key 密钥
     * @param charset 编码
     * @param algorithm 加密算法
     * @return 解密后的内容
     * @throws Exception 异常
     */
    public static String decryptAfterHexDecode(String content, String key, String charset, AlgorithmType algorithm) throws Exception {
        AssertUtil.assertTrue(null != content && content.length() != 0, "解密内容不能为空");
        AssertUtil.assertTrue(null != key && key.length() != 0, "秘钥不能为空");
        byte[] decodeBuffer = ByteUtil.hexTBytes(content);
        byte[] decryptTBytes = decryptBytes(decodeBuffer, key.getBytes(), algorithm);
        String originalString = new String(decryptTBytes, charset);
        return originalString;
    }

    /**
     * 解密
     *
     * @param content 密文
     * @param key 密钥
     * @param algorithm 加密算法
     * @return 解密后的内容
     * @throws Exception 异常
     */
    public static byte[] decryptBytes(byte[] content, byte[] key, AlgorithmType algorithm) throws Exception {
        AssertUtil.assertTrue(null != content && content.length != 0, "解密内容不能为空");
        AssertUtil.assertNotNull(algorithm, "解密算法不能为空");
        AssertUtil.assertTrue(null != key && key.length >= DEFAULT_KEY_BYTE_LENGTH, "秘钥长度至少为16位");
        SecretKeySpec skeySpec = new SecretKeySpec(key, AES);
        Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
        if (hasIv(algorithm)) {
            IvParameterSpec iv = new IvParameterSpec(MODEL.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        }
        byte[] original = cipher.doFinal(content);
        return original;
    }

    /**
     * 判断所使用的加密算法是否需要向量
     * @param algorithm 算法
     * @return result
     */
    private static boolean hasIv(AlgorithmType algorithm) {
        return AlgorithmType.AES_CBC_NOPADDING == algorithm || AlgorithmType.AES_CBC_PKCS5Padding == algorithm;
    }
}
