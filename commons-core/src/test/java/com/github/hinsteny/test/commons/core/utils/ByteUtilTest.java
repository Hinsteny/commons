package com.github.hinsteny.test.commons.core.utils;

import java.io.UnsupportedEncodingException;
import com.github.hinsteny.commons.core.utils.ByteUtil;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version ByteUtilTest: ByteUtilTest 2019-05-08 17:36 All rights reserved.$
 */
public class ByteUtilTest {

    @Test
    public void testTyteToHex() throws UnsupportedEncodingException {
        String data = "走遍世界的心不能停...O(∩_∩)O哈哈~...", hex;
        byte[] bytes = data.getBytes("UTF-8");
        hex = ByteUtil.byteToHex(bytes);
        System.out.println(hex);
    }

}
