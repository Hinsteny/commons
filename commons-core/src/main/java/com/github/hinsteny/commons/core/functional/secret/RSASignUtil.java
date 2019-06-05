package com.github.hinsteny.commons.core.functional.secret;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * RSA签名,加解密处理，注意：密钥长度1024
 *
 * @author Hinsteny
 * @version RSASignUtil: RSASignUtil 2019-05-10 10:14 All rights reserved.$
 */
public class RSASignUtil {

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public enum Algorithm {

        MD5withRSA("MD5withRSA"),
        SHA1withRSA("SHA1withRSA"),
        ;

        private String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return KeyPair
     * @throws Exception 异常
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
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
     * 签名字符串
     *
     * @param param 需要签名的数据
     * @param privateKey 私钥(BASE64编码)
     * @param charset 编码格式
     * @return 签名结果(BASE64编码)
     * @throws Exception 异常
     */
    public static String sign(Map<String, String> param, String privateKey, String charset) throws Exception {
        return sign(Algorithm.SHA1withRSA, buildParam(param), privateKey, charset);
    }

    /**
     * 签名字符串
     *
     * @param algorithm 签名算法
     * @param param 需要签名的数据
     * @param privateKey 私钥(BASE64编码)
     * @param charset 编码格式
     * @return 签名结果(BASE64编码)
     * @throws Exception 异常
     */
    public static String sign(Algorithm algorithm, Map<String, String> param, String privateKey, String charset) throws Exception {
        return sign(algorithm, buildParam(param), privateKey, charset);
    }

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     * @param charset 编码格式
     * @return 签名结果(BASE64编码)
     * @throws Exception 异常
     */
    public static String sign(String text, String privateKey, String charset) throws Exception {
        return doSign(Algorithm.SHA1withRSA, text, privateKey, charset);
    }

    /**
     * 签名字符串
     *
     * @param algorithm 签名算法
     * @param text 需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     * @param charset 编码格式
     * @return 签名结果(BASE64编码)
     * @throws Exception 异常
     */
    public static String sign(Algorithm algorithm, String text, String privateKey, String charset) throws Exception {
        return doSign(algorithm, text, privateKey, charset);
    }

    /**
     * 进行RSA签名
     *
     * @param algorithm 签名算法
     * @param text 需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     * @param charset 编码格式
     * @return 签名结果(BASE64编码)
     * @throws Exception 异常
     */
    private static String doSign(Algorithm algorithm, String text, String privateKey, String charset) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(algorithm.getAlgorithm());
        signature.initSign(privateK);
        signature.update(getContentBytes(text, charset));
        byte[] result = signature.sign();

        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 验签
     *
     * @param text 需要签名的字符串
     * @param sign 客户签名结果
     * @param publicKey 公钥(BASE64编码)
     * @param charset 编码格式
     * @return 验签结果
     * @throws Exception 异常
     */
    public static boolean verify(String text, String sign, String publicKey, String charset) throws Exception {
        return verify(Algorithm.SHA1withRSA, text, sign, publicKey, charset);
    }

    /**
     * 验签
     *
     * @param algorithm 签名算法
     * @param text 需要签名的字符串
     * @param sign 客户签名结果
     * @param publicKey 公钥(BASE64编码)
     * @param charset 编码格式
     * @return 验签结果
     * @throws Exception 异常
     */
    public static boolean verify(Algorithm algorithm, String text, String sign, String publicKey, String charset) throws Exception {
        return doVerify(algorithm, text, sign, publicKey, charset);
    }

    /**
     * 进行RSA验签
     *
     * @param algorithm 签名算法
     * @param text 需要签名的字符串
     * @param sign 客户签名结果
     * @param publicKey 公钥(BASE64编码)
     * @param charset 编码格式
     * @return 验签结果
     * @throws Exception 异常
     */
    private static boolean doVerify(Algorithm algorithm, String text, String sign, String publicKey, String charset) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(algorithm.getAlgorithm());
        signature.initVerify(publicK);
        signature.update(getContentBytes(text, charset));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * @param content origin content
     * @param charset get byte encoding
     * @return content bytes
     * @throws UnsupportedEncodingException 异常
     */
    private static byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (null == charset || "".equals(charset.trim())) {
            return content.getBytes();
        }
        return content.getBytes(charset);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String buildParam(Map<String, String> params) {
        StringBuilder sbr = new StringBuilder();
        if (null != params && params.size() > 0) {
            Map<String, String> sortMap = new TreeMap<>(String::compareTo);
            sortMap.putAll(params);
            final String KVItem = "%s=%s&";
            sortMap.forEach((key, value) -> sbr.append(String.format(KVItem, key, value)));
            sbr.setLength(sbr.length() - 1);
        }

        return sbr.toString();
    }

}
