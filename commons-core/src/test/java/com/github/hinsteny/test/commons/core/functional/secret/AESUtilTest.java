package com.github.hinsteny.test.commons.core.functional.secret;

import com.github.hinsteny.commons.core.functional.secret.AESUtil;
import com.github.hinsteny.commons.core.functional.secret.AlgorithmType;
import com.github.hinsteny.commons.core.utils.ByteUtil;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version AESUtilTest: AESUtilTest 2019-05-09 09:15 All rights reserved.$
 */
public class AESUtilTest {

    @Test(testName = "使用AES工具对字符串进行加解密示例")
    public void testAesUtilEncryptAndDecryptToStr() throws Exception {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~", key, secret, origin;
        int keyLen = 128;
        key = AESUtil.generateAESKey();
        System.out.println(String.format("key length %d: %s", keyLen, key));
        System.out.println("============== 默认AES加解密前后使用Base64加解码 ==============");
        secret = AESUtil.encrypt(data, key);
        System.out.println(String.format("encrypt data: %s, result: %s", data, secret));
        origin = AESUtil.decrypt(secret, key);
        System.out.println("decrypt result: " + origin);
        System.out.println("============== 指定AES加解密前后使用Hex加解码 ==============");
        secret = AESUtil.encryptThenHex(data, key, "UTF-8", AlgorithmType.AES_CBC_NOPADDING);
        System.out.println(String.format("encrypt data: %s, result: %s", data, secret));
        origin = AESUtil.decryptAfterHexDecode(secret, key, "UTF-8", AlgorithmType.AES_CBC_NOPADDING);
        System.out.println("decrypt result: " + origin);
    }

    @Test(testName = "使用AES工具对字符串进行加解密示例")
    public void testAesUtilEncryptAndDecryptToByte() throws Exception {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~";
        int keyLen = 128;
        byte[] key = AESUtil.generateAESKeyByte(keyLen);
        System.out.println(String.format("key length %d: %s", keyLen, key));
        byte[] secret = AESUtil.encryptBytes(data.getBytes("UTF-8"), key, AlgorithmType.AES_ECB_NoPadding);
        System.out.println(String.format("encrypt data: %s, result: ", data) + ByteUtil.byteToStr(secret));
        byte[] origin = AESUtil.decryptBytes(secret, key, AlgorithmType.AES_ECB_NoPadding);
        System.out.println("decrypt result: " + new String(origin));
    }

}
