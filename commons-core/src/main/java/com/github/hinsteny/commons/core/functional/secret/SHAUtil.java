package com.github.hinsteny.commons.core.functional.secret;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA 相关hash处理工具类
 *
 * @author Hinsteny
 * @version SHAUtil: SHAUtil 2019-05-10 10:15 All rights reserved.$
 */
public class SHAUtil {

    /**
     * 签名算法
     */
    public enum SignType {

        SHA1("SHA1"),
        SHA224("SHA-224"),
        SHA256("SHA-256"),
        SHA384("SHA-384"),
        SHA512("SHA-512"),
        ;

        private String algorithm;

        SignType(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    /**
     * 对文本进行512分组计算最终生成40位的消息摘要
     *
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    public static String calculateSha1Val(String content) throws NoSuchAlgorithmException {
        return calculateSignVal(SignType.SHA1, content);
    }

    /**
     * 对文本进行512分组计算最终生成56位的消息摘要
     *
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    public static String calculateSha224Val(String content) throws NoSuchAlgorithmException {
        return calculateSignVal(SignType.SHA224, content);
    }

    /**
     * 对文本进行512分组计算最终生成64位的消息摘要
     *
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    public static String calculateSha256Val(String content) throws NoSuchAlgorithmException {
        return calculateSignVal(SignType.SHA256, content);
    }

    /**
     * 对文本进行512分组计算最终生成96位的消息摘要
     *
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    public static String calculateSha384Val(String content) throws NoSuchAlgorithmException {
        return calculateSignVal(SignType.SHA384, content);
    }

    /**
     * 对文本进行512分组计算最终生成128位的消息摘要
     *
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    public static String calculateSha512Val(String content) throws NoSuchAlgorithmException {
        return calculateSignVal(SignType.SHA512, content);
    }

    /**
     * 根据指定的签名算法, 对输入内容进行签名
     * @param type 散列算法
     * @param content 被散列内容
     * @return 加密后的内容
     * @throws NoSuchAlgorithmException 异常
     */
    private static String calculateSignVal (SignType type, String content) throws NoSuchAlgorithmException {
        MessageDigest sha1Encoder = MessageDigest.getInstance(type.getAlgorithm());
        sha1Encoder.update(content.getBytes());
        byte[] encodedBytes = sha1Encoder.digest();
        StringBuilder resultBuffer = new StringBuilder();
        for (byte perByte : encodedBytes) {
            String hex = Integer.toHexString(perByte & 0xff);
            resultBuffer.append((hex.length() > 1 ? "" : "0").concat(hex));
        }
        return resultBuffer.toString();
    }

}
