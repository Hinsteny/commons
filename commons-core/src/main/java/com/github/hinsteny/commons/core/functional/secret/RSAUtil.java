package com.github.hinsteny.commons.core.functional.secret;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import com.github.hinsteny.commons.core.utils.ByteUtil;

/**
 * RSA 加解密工具
 * PS:
 * 1. 公钥216位, 私钥844位
 * 2. 此工具没有实现超长数据分片加密功能, 因此被加密的数据不超过117位
 *
 * @author Hinsteny
 * @version RSAUtil: RSAUtil 2019-05-10 10:15 All rights reserved.$
 */
public class RSAUtil {

    /**
     * 签名算法
     */
    public static final String ALGORITHM = "RSA";

    /**
     * 算法实现
     */
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return KeyPair
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;

    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyPair 密钥对
     * @return public key
     */
    public static String getPublicKey(KeyPair keyPair) {
        Key key = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyPair 密钥对
     * @return private key
     */
    public static String getPrivateKey(KeyPair keyPair) {
        Key key = keyPair.getPrivate();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 使用给定key, 对字符串数据按照指定编码, 进行加密
     * @param key
     * @param data
     * @param charset
     * @return
     * @throws Exception
     */
    public static String encryptData(String key, String data, String charset) throws Exception {
        return encryptData(key, data, charset, true);
    }

    public static String encryptData(String key, String data, String charset, boolean usePubKey) throws Exception {
        byte[] dataBytes = data.getBytes(charset);
        byte[] encryptData = encryptData(key, dataBytes, usePubKey);
        return ByteUtil.byteToHex(encryptData);
    }

    public static byte[] encryptData(String key, byte[] content, boolean usePubKey) throws Exception {
        return encrypt(content, buildKey(key, usePubKey));
    }

    /**
     * 使用给定key, 对字符串数据按照指定编码, 进行解密
     * @param key
     * @param data
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decryptData(String key, String data, String charset) throws Exception {
        return decryptData(key, data, charset, false);
    }

    public static String decryptData(String key, String data, String charset, boolean usePubKey) throws Exception {
        byte[] dataBytes = ByteUtil.hexTBytes(data.getBytes(charset));
        byte[] encryptData = decryptData(key, dataBytes, usePubKey);
        return new String(encryptData, charset);
    }

    public static byte[] decryptData(String key, byte[] content, boolean usePubKey) throws Exception {
        return decrypt(content, buildKey(key, usePubKey));
    }

    /**
     * [公钥/私钥]加密
     * @param content
     * @param key [PublicKey/PrivateKey]
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] content, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content);
    }

    /**
     * [公钥/私钥]解密
     * @param content
     * @param key [PublicKey/PrivateKey]
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] content, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(content);
    }

    /**
     * 根据原始密钥串构建key对象
     * @param key
     * @param isPubKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static Key buildKey(String key, boolean isPubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyByte = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key generateKey;
        if (isPubKey) {
            KeySpec keySpec = new X509EncodedKeySpec(keyByte);
            generateKey = keyFactory.generatePublic(keySpec);
        } else {
            KeySpec keySpec = new PKCS8EncodedKeySpec(keyByte);
            generateKey = keyFactory.generatePrivate(keySpec);
        }
        return generateKey;
    }

}
