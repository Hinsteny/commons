package com.github.hinsteny.commons.warp.senstive;

import com.alibaba.fastjson.JSON;

/**
 * 脱敏工具类.
 *
 * @author Hinsteny
 * @version SensitiveUtil: 2019-08-12 14:51 All rights reserved.$
 */
public class SensitiveUtil {

    public static String toJson(Object object) {
        return JSON.toJSONString(object, SensitiveValueFilter.INSTANCE.getInstance());
    }

}
