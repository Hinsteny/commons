package com.github.hinsteny.test.commons.core.functional.secret;

import com.github.hinsteny.commons.core.functional.secret.DSASignUtil;
import java.security.KeyPair;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version DSASignUtilTest: DSASignUtilTest 2019-06-04 12:21 All rights reserved.$
 */
public class DSASignUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Test(testName = "使用DSA工具对字符串进行签名验签示例")
    public void testAesUtilEncryptAndDecryptToStr() throws Exception {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~", publicKey, privateKey, secret;
        boolean result;
        int keyLen = 1024;
        KeyPair keyPair = DSASignUtil.generateKeyPair();
        publicKey = DSASignUtil.getPublicKey(keyPair);
        privateKey = DSASignUtil.getPrivateKey(keyPair);
        System.out.println(String.format("key length %d: %s", keyLen, privateKey));
        System.out.println("============== 默认DSA签名前后使用Base64加解码 ==============");
        secret = DSASignUtil.sign(data, privateKey, DEFAULT_CHARSET);
        System.out.println(String.format("sign data: %s, result: %s", data, secret));
        result = DSASignUtil.verify(data, secret, publicKey, DEFAULT_CHARSET);
        System.out.println("verify result: " + result);
    }

}
