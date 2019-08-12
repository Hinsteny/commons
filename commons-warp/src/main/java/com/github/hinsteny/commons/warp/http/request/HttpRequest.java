package com.github.hinsteny.commons.warp.http.request;

import com.github.hinsteny.commons.warp.http.protocol.PreHandleResponse;
import org.apache.http.Header;
import java.util.List;
import java.util.Map;

/**
 * Http封装的request.
 *
 * @author Hinsteny
 * @version HttpRequest: 2019-08-12 11:43 All rights reserved.$
 */
public class HttpRequest {

    /**
     * URI
     */
    private transient String uri;

    /**
     * http方式
     */
    private transient MethodType methodType;

    /**
     * http请求参数类型
     */
    private transient ParamType paramType;

    /**
     * http请求头
     */
    private transient List<Header> headers;

    /**
     * Get和Post的参数，Form参数
     */
    private transient Map<String, String> paramsMap;

    /**
     * Text类型的Body
     */
    private transient String bodyText;

    /**
     * 建立http连接超时时间
     */
    private transient int connectTimeout = -1;

    /**
     * socket通讯超时时间
     */
    private transient int socketTimeout = -1;

    /**
     * 对于httpclient
     */
    private transient PreHandleResponse preHandleResponse;

    public HttpRequest(String uri, String bodyText) {
        this(uri, MethodType.POST, bodyText);
    }

    public HttpRequest(String uri, MethodType methodType, String bodyText) {
        this(uri, methodType, ParamType.TEXT, bodyText);
    }

    public HttpRequest(String uri, MethodType methodType, ParamType paramType, String bodyText) {
        this(uri, methodType, null, paramType, null, bodyText);
    }

    public HttpRequest(String uri, MethodType methodType, List<Header> headers, String bodyText) {
        this(uri, methodType, headers, ParamType.TEXT, null, bodyText);
    }

    public HttpRequest(String uri, Map<String, String> paramsMap) {
        this(uri, MethodType.POST, paramsMap);
    }

    public HttpRequest(String uri, List<Header> headers, Map<String, String> paramsMap) {
        this(uri, MethodType.POST, headers, ParamType.APPLICATION_JSON, paramsMap);
    }

    public HttpRequest(String uri, MethodType methodType, Map<String, String> paramsMap) {
        this(uri, methodType, null, paramsMap);
    }

    public HttpRequest(String uri, MethodType methodType, ParamType paramType, Map<String, String> paramsMap) {
        this(uri, methodType, null, paramType, paramsMap);
    }

    public HttpRequest(String uri, MethodType methodType, List<Header> headers, ParamType paramType, Map<String, String> paramsMap) {
        this(uri, methodType, headers, paramType, paramsMap, null);
    }

    public HttpRequest(String uri, MethodType methodType, List<Header> headers, ParamType paramType, Map<String, String> paramsMap, String bodyText) {
        this.uri = uri;
        this.methodType = methodType;
        this.headers = headers;
        this.paramType = paramType;
        this.paramsMap = paramsMap;
        this.bodyText = bodyText;
    }

    public String getUri() {
        return uri;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void setParamType(ParamType paramType) {
        this.paramType = paramType;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public PreHandleResponse getPreHandleResponse() {
        return preHandleResponse;
    }

    public void setPreHandleResponse(PreHandleResponse preHandleResponse) {
        this.preHandleResponse = preHandleResponse;
    }

}
