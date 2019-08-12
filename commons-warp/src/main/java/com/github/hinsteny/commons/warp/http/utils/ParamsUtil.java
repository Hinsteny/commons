package com.github.hinsteny.commons.warp.http.utils;

import com.github.hinsteny.commons.core.exception.BusinessException;
import com.github.hinsteny.commons.core.utils.ReflectUtil;
import com.github.hinsteny.commons.warp.http.exception.HttpErrorCode;
import com.github.hinsteny.commons.warp.http.request.IgnoreParam;
import com.google.common.base.Strings;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http参数工具类.
 *
 * @author Hinsteny
 * @version ParamsUtil: 2019-08-12 15:39 All rights reserved.$
 */
public abstract class ParamsUtil {

    private static final Logger logger = LoggerFactory.getLogger(ParamsUtil.class);

    public ParamsUtil() {
    }

    /**
     * 把键值对集合拼接成http-get请求url参数
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getParamsString(Map<String, String> paramsMap) {
        if (null == paramsMap || paramsMap.size() == 0) {
            return "";
        }
        final StringBuilder result = new StringBuilder("?");
        paramsMap.forEach((key, value)->{
            try {
                result.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("拼接http-get请求字符串异常");
            }
        });

        return result.substring(0, result.length() - 1);
    }

    /**
     * 把一个对象类中的属性转化为键值对
     * @param object
     * @return
     * @throws BusinessException
     */
    public static Map<String, String> toParamMap(Object object) throws BusinessException {
        Map<String, String> paramsMap = new HashMap<>(16);
        try {
            Field[] declaredFields = object.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(object) == null ? Strings.nullToEmpty(null) : field.get(object).toString());
            }
        } catch (Exception e) {
            logger.error("异常", e);
            throw new BusinessException(HttpErrorCode.BEAN_CONVERT_TO_MAP_ERROR.getCode(), "转化http参数出错");
        }
        return paramsMap;
    }

    /**
     * 把一个对象类中的属性转化为键值对
     * @param object
     * @return
     * @throws BusinessException
     */
    public static Map<String, String> toParamMapIgnore(Object object) throws BusinessException {
        Map<String, String> paramsMap = new HashMap<>(16);
        try {
            List<Field> declaredFields = ReflectUtil.getAccessibleFields(object);
            for (Field field : declaredFields) {
                field.setAccessible(true);
                //如果加了transient或者忽略注解，就忽略掉这些Field参数
                if (!Modifier.isTransient(field.getModifiers()) && field.getAnnotation(IgnoreParam.class) == null) {
                    paramsMap.put(field.getName(), field.get(object) == null ? Strings.nullToEmpty(null) : field.get(object).toString());
                }
            }
        } catch (Exception e) {
            logger.error("异常", e);
            throw new BusinessException(HttpErrorCode.BEAN_CONVERT_TO_MAP_ERROR.getCode(), "转化http参数出错");
        }
        return paramsMap;
    }
}
