package com.github.hinsteny.commons.warp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.hinsteny.commons.core.utils.StringUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hinsteny
 * @version MapUtil: 2019-08-12 14:09 All rights reserved.$
 */
public abstract class MapUtil {

    public MapUtil() {
    }

    /**
     * 判断map是否为空
     * @param map map
     * @return result
     */
    public static boolean isEmpty(Map<? extends Object, ? extends Object> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 判断map是否不为空
     * @param map map
     * @return result
     */
    public static boolean isNotEmpty(Map<? extends Object, ? extends Object> map) {
        return !isEmpty(map);
    }

    /**
     * jsonStr to map
     * @param ext jsonStr
     * @return map
     */
    public static Map<String, String> jsonToMap(String ext) {
        Map<String, String> extMap = StringUtil.isBlank(ext) ? new HashMap(16) : JSON.parseObject(ext, new TypeReference<Map<String, String>>() {
        }, new Feature[0]);
        return extMap;
    }

    /**
     * map to jsonStr
     * @param data map
     * @return jsonStr
     */
    public static String mapToJson(Map<? extends Object, ? extends Object> data) {
        return MapUtil.isEmpty(data) ? null : JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.UseISO8601DateFormat});
    }

}
