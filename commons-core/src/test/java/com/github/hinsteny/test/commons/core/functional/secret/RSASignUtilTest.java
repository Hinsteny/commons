package com.github.hinsteny.test.commons.core.functional.secret;

import com.github.hinsteny.commons.core.functional.secret.RSASignUtil;
import com.github.hinsteny.commons.core.functional.secret.RSASignUtil.Algorithm;
import java.security.KeyPair;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version RSASignUtilTest: RSASignUtilTest 2019-06-05 08:42 All rights reserved.$
 */
public class RSASignUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Test(testName = "使用DSA工具对字符串进行签名验签示例")
    public void testRSASignUtilSignAndVerifyForStr() throws Exception {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~", publicKey, privateKey, secret;
        boolean result;
        int keyLen = 1024;
        KeyPair keyPair = RSASignUtil.generateKeyPair();
        publicKey = RSASignUtil.getPublicKey(keyPair);
        privateKey = RSASignUtil.getPrivateKey(keyPair);
        System.out.println(String.format("key length %d: %s", keyLen, privateKey));
        System.out.println("============== 默认RSA签名前后使用Base64加解码 ==============");
        System.out.println("============== 使用默认签名算法 SHA1withRSA ==============");
        secret = RSASignUtil.sign(data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = RSASignUtil.verify(data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
        System.out.println("============== 使用非默认签名算法 MD5withRSA ==============");
        secret = RSASignUtil.sign(Algorithm.MD5withRSA, data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = RSASignUtil.verify(Algorithm.MD5withRSA, data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
    }

}
