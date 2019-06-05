package com.github.hinsteny.test.commons.core.functional.secret;

import com.github.hinsteny.commons.core.functional.secret.ECDSASignUtil;
import com.github.hinsteny.commons.core.functional.secret.ECDSASignUtil.Algorithm;
import java.security.KeyPair;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version ECDSASignUtilTest: ECDSASignUtilTest 2019-06-05 08:21 All rights reserved.$
 */
public class ECDSASignUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Test(testName = "使用ECDSA工具对字符串进行签名验签示例")
    public void testECDSASignUtilSignAndVerifyForStr() throws Exception {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~", publicKey, privateKey, secret;
        boolean result;
        KeyPair keyPair = ECDSASignUtil.generateKeyPair();
        publicKey = ECDSASignUtil.getPublicKey(keyPair);
        privateKey = ECDSASignUtil.getPrivateKey(keyPair);
        System.out.println(String.format("key length %d: %s", privateKey.length(), privateKey));
        System.out.println("============== 默认ECDSA签名前后使用Base64加解码 ==============");
        System.out.println("============== 使用默认签名算法 SHA256withECDSA ==============");
        secret = ECDSASignUtil.sign(data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = ECDSASignUtil.verify(data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
        System.out.println("============== 使用非默认签名算法 NONEwithECDSA ==============");
        secret = ECDSASignUtil.sign(Algorithm.NONEwithECDSA, data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = ECDSASignUtil.verify(Algorithm.NONEwithECDSA, data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
        System.out.println("============== 使用非默认签名算法 SHA1withECDSA ==============");
        secret = ECDSASignUtil.sign(Algorithm.SHA1withECDSA, data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = ECDSASignUtil.verify(Algorithm.SHA1withECDSA, data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
    }

}
