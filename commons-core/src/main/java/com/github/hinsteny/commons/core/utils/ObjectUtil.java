package com.github.hinsteny.commons.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hinsteny
 * @version ObjectUtil: ObjectUtil 2019-05-09 15:51 All rights reserved.$
 */
public class ObjectUtil {

    private static final Logger LOGGER = LogManager.getLogger(ObjectUtil.class);

    /**
     * 判断class类型是否为基础类型的包装引用类型
     * [byte, short, int, long, float, double, boolean, char]
     *                        ||||||
     * [Byte, Short, Integer, Long, Float, Double, Boolean, Character]
     * @param cls 被判断的类型
     * @return
     */
    public static boolean isPrimitive(Class cls) {
        try {
            return cls.isPrimitive() ? true : ((Class<?>) cls.getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.warn("judge class type is primitive exception", e);
        }
        return false;
    }

}
