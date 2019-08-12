package com.github.hinsteny.commons.warp.http.protocol;

import org.apache.http.HttpResponse;

/**
 * @author Hinsteny
 * @version PreHandleResponse: 2019-08-12 11:44 All rights reserved.$
 */
public interface PreHandleResponse {

    /**
     * 预处理http response.
     *
     * @param httpResponse response result
     */
    String preHandle(HttpResponse httpResponse) throws Exception;

}
