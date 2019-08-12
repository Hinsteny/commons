package com.github.hinsteny.commons.warp.http.request;

/**
 * 参数Body类型.
 *
 * @author Hinsteny
 * @version ParamType: 2019-08-12 13:46 All rights reserved.$
 */
public enum ParamType {

    /**
     * form表单
     */
    X_WWW_FORM_URLENCODED,

    /**
     * post body
     */
    APPLICATION_JSON,

    APPLICATION_XML,

    TEXT,

    /**
     * url query
     */
    QUERY,
    ;

}
