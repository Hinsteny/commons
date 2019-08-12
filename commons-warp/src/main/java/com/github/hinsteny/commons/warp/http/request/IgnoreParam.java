package com.github.hinsteny.commons.warp.http.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略的参数，不会放到Request的Map中.
 *
 * @author Hinsteny
 * @version IgnoreParam: 2019-08-12 15:43 All rights reserved.$
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreParam {
}
