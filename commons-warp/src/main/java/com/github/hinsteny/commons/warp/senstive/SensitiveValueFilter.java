package com.github.hinsteny.commons.warp.senstive;

import com.alibaba.fastjson.serializer.ValueFilter;
import java.lang.reflect.Field;

/**
 * @author Hinsteny
 * @version SensitiveValueFilter: 2019-08-12 14:51 All rights reserved.$
 */
public enum SensitiveValueFilter {

    INSTANCE;

    private ValueFilter instance;

    SensitiveValueFilter() {
        instance = (object, name, value) -> {
            try {
                Field field = object.getClass().getDeclaredField(name);
                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                SensitiveType type;
                if (value instanceof String && null != sensitive && (type = sensitive.type()) != null) {
                    String result = replaceSensitive(value.toString(), type.getRegex(), type.getReplacement());
                    if (null != result) {
                        value = result;
                    }
                }
            } catch (NoSuchFieldException e) {
                //无字段不做操作
            }
            return value;
        };

    }

    public ValueFilter getInstance() {
        return instance;
    }

    public static String replaceSensitive(String original, String regex, String replacement) {
        try {
            String result = original.replaceAll(regex, replacement);
            return result;
        } catch (Exception e) {

        }
        return null;
    }

}
