package com.github.hinsteny.commons.core.functional.secret;

/**
 * @author Hinsteny
 * @version AlgorithmType: AlgorithmType 2019-05-08 18:55 All rights reserved.$
 */
public enum AlgorithmType {

    AES_CBC_NOPADDING("AES/CBC/NoPadding"),
    AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
    AES_ECB_NoPadding("AES/ECB/NoPadding"),
    AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),

    DES_CBC_NoPadding("DES/CBC/NoPadding"),
    DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding"),
    DES_ECB_NoPadding("DES/ECB/NoPadding"),
    DES_ECB_PKCS5Padding("DES/ECB/PKCS5Padding"),

    DESede_CBC_NoPadding("DESede/CBC/NoPadding"),
    DESede_CBC_PKCS5Padding("DESede/CBC/PKCS5Padding"),
    DESede_ECB_NoPadding("DESede/ECB/NoPadding"),
    DESede_ECB_PKCS5Padding("DESede/ECB/PKCS5Padding"),

    RSA_ECB_PKCS1Padding("RSA/ECB/PKCS1Padding"),
    RSA_ECB_OAEPWithSHA_1AndMGF1Padding("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
    RSA_ECB_OAEPWithSHA_256AndMGF1Padding("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),

    ;

    private String algorithm;

    AlgorithmType(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

}
