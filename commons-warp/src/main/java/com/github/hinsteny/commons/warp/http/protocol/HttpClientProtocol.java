package com.github.hinsteny.commons.warp.http.protocol;

import com.github.hinsteny.commons.warp.http.exception.HttpException;
import com.github.hinsteny.commons.warp.http.request.HttpRequest;
import java.util.Map;

/**
 * http请求定义接口.
 *
 * @author Hinsteny
 * @version HttpClientProtocol: 2019-08-12 11:25 All rights reserved.$
 */
public interface HttpClientProtocol {

    /**
     * 发送报文
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    byte[] sendBackByte(HttpRequest request) throws HttpException;

    /**
     * 发送报文
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    String sendBackString(HttpRequest request) throws HttpException;

    /**
     * 发送报文
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    Map<String, String> sendBackMap(HttpRequest request) throws HttpException;

}
