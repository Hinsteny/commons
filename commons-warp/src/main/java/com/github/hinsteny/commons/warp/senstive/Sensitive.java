package com.github.hinsteny.commons.warp.senstive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感信息格式化方式.
 *
 * @author Hinsteny
 * @version Sensitive: 2019-08-12 14:49 All rights reserved.$
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    SensitiveType type();
}
