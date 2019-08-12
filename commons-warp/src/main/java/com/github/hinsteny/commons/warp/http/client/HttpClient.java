package com.github.hinsteny.commons.warp.http.client;

import com.alibaba.fastjson.JSONArray;
import com.github.hinsteny.commons.core.utils.StringUtil;
import com.github.hinsteny.commons.warp.http.exception.HttpErrorCode;
import com.github.hinsteny.commons.warp.http.exception.HttpException;
import com.github.hinsteny.commons.warp.http.protocol.HttpClientProtocol;
import com.github.hinsteny.commons.warp.http.request.HttpRequest;
import com.github.hinsteny.commons.warp.http.request.MethodType;
import com.github.hinsteny.commons.warp.http.request.ParamType;
import com.github.hinsteny.commons.warp.http.request.ResponseType;
import com.github.hinsteny.commons.warp.http.utils.ParamsUtil;
import com.github.hinsteny.commons.warp.senstive.SensitiveUtil;
import com.github.hinsteny.commons.warp.utils.MapUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http和https协议的client.
 *
 * @author Hinsteny
 * @version HttpClient: 2019-08-12 11:24 All rights reserved.$
 */
public class HttpClient implements HttpClientProtocol {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public static final int HTTP_STATUS_200 = 200;
    public static final int HTTP_STATUS_302 = 302;

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIME_OUT = 10000;
    private static final int SOCKET_TIME_OUT = 10000;

    private CloseableHttpClient httpClients;

    public HttpClient(CloseableHttpClient httpClients) {
        this.httpClients = httpClients;
    }

    /**
     * 发送报文
     *
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    @Override
    public byte[] sendBackByte(HttpRequest request) throws HttpException {
        return (byte[]) send(request, ResponseType.BYTE);
    }

    /**
     * 发送报文
     *
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    @Override
    public String sendBackString(HttpRequest request) throws HttpException {
        return (String) send(request, ResponseType.STRING);
    }

    /**
     * 发送报文
     *
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     */
    @Override
    public Map<String, String> sendBackMap(HttpRequest request) throws HttpException {
        return (Map<String, String>) send(request, ResponseType.BODY);
    }

    /**
     * 发送报文
     *
     * @param request 拦截方法
     * @return 返回参数 统一返回json结果
     * @throws HttpException HttpException
     */
    public Object send(HttpRequest request, ResponseType responseType) throws HttpException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CloseableHttpResponse httpResponse = null;
        //返回值处理
        try {
            httpResponse = doHttpExecute(request);
            switch (responseType) {
                case BYTE: {
                    return getResponseByte(request, httpResponse);
                }
                case STRING: {
                    return getResponseStr(request, httpResponse);
                }
                case BODY: {
                    String responseStr = getResponseStr(request, httpResponse);
                    Map<String, String> response = new HashMap<>(8);
                    Map<String, String> map = null;
                    try {
                        if (StringUtil.isNotBlank(responseStr)) {
                            if (responseStr.startsWith("{") || responseStr.startsWith("[")) {
                                map = MapUtil.jsonToMap(responseStr);
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("Parse response str: [{}] to json failed..", responseStr, e);
                    }
                    if (MapUtil.isNotEmpty(map)) {
                        response.putAll(map);
                    } else {
                        response.put("response", responseStr);
                    }
                    return response;
                }
                default:
                    throw new HttpException(HttpErrorCode.PARAM_TYPE_NOT_SUPPORT);
            }
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Handle the response content error, error={}", e);
            throw new HttpException(HttpErrorCode.HANDLE_RESPONSE_CONTENT_ERROR);
        } finally {
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    logger.warn("Close the response error", e);
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("Http request response: uri={}, time={} ", request.getUri(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
        }
    }

    private CloseableHttpResponse doHttpExecute(HttpRequest request) throws HttpException {
        //设置超时时间，如果request没设置超时时间，取profile里的
        int socketTimeout = request.getSocketTimeout() > 0 ? request.getSocketTimeout() : CONNECT_TIME_OUT;
        int connectTimeout = request.getConnectTimeout() > 0 ? request.getConnectTimeout() : SOCKET_TIME_OUT;
        RequestConfig config = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

        if (logger.isInfoEnabled()) {
            logger.info("Http request params: uri=[{}], headers=[{}], params=[{}], body=[{}]", request.getUri(), JSONArray.toJSONString(request.getHeaders()),
                SensitiveUtil.toJson(request.getParamsMap()), request.getBodyText());
        }
        //请求调用
        CloseableHttpResponse httpResponse;
        if (request.getMethodType() == MethodType.GET) {
            httpResponse = sendGet(request, config);
        } else if (request.getMethodType() == MethodType.POST) {
            httpResponse = sendPost(request, config);
        } else {
            throw new HttpException(HttpErrorCode.CHECK_REQUEST_ERROR);
        }
        //校验http状态码
        checkHttpStatus(httpResponse);
        return httpResponse;
    }

    /**
     * 进行远程get请求
     *
     * @param request request
     * @param config config
     * @throws HttpException HttpException
     */
    private CloseableHttpResponse sendGet(HttpRequest request, RequestConfig config) throws HttpException {
        String url = request.getUri() + ParamsUtil.getParamsString(request.getParamsMap());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        if (null != request.getHeaders() && request.getHeaders().size() > 0) {
            Header[] headers = new Header[request.getHeaders().size()];
            httpGet.setHeaders(request.getHeaders().toArray(headers));
        }
        CloseableHttpResponse response;
        try {
            response = httpClients.execute(httpGet);
        } catch (IOException e) {
            logger.error("Send http request IOException, error={}", e);
            throw new HttpException(HttpErrorCode.ASK_SERVICE_ERROR);
        }
        return response;
    }

    /**
     * 进行远程Post请求
     *
     * @param request request
     * @param config config
     * @throws HttpException HttpException
     */
    private CloseableHttpResponse sendPost(HttpRequest request, RequestConfig config) throws HttpException {
        //判断参数类型
        String url = request.getUri();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        if (null != request.getHeaders() && request.getHeaders().size() > 0) {
            Header[] headers = new Header[request.getHeaders().size()];
            httpPost.setHeaders(request.getHeaders().toArray(headers));
        }
        CloseableHttpResponse response;
        if (request.getParamType() == ParamType.APPLICATION_JSON) {
            StringEntity entity = null;
            if (StringUtil.isNotBlank(request.getBodyText())) {
                entity = new StringEntity(request.getBodyText(), ContentType.APPLICATION_JSON);
            } else if (MapUtil.isNotEmpty(request.getParamsMap())) {
                entity = new StringEntity(MapUtil.mapToJson(request.getParamsMap()), ContentType.APPLICATION_JSON);
            }
            httpPost.setEntity(entity);
        } else if (request.getParamType() == ParamType.X_WWW_FORM_URLENCODED) {
            List<NameValuePair> pairList = Lists.newArrayList();
            if (request.getParamsMap() == null) {
                throw new HttpException(HttpErrorCode.PARAM_TYPE_NOT_SUPPORT, "请求参数不能为空");
            }
            buildPairByMap(pairList, request.getParamsMap());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, Consts.UTF_8);
            httpPost.setEntity(entity);
        } else if (request.getParamType() == ParamType.TEXT) {
            StringEntity entity = new StringEntity(request.getBodyText(), Charset.forName("UTF-8"));
            httpPost.setEntity(entity);
        } else if (request.getParamType() == ParamType.QUERY) {
            if (StringUtil.isNotBlank(request.getBodyText())) {
                String postUrl;
                try {
                    postUrl = String.format("%s?%s", request.getUri(), URLEncoder.encode(request.getBodyText(), "UTF-8"));
                    if (logger.isInfoEnabled()) {
                        logger.info("Http request post-url: [{}]", postUrl);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new HttpException(HttpErrorCode.PARAM_URLENCODE_ERROR);
                }
                httpPost = new HttpPost(postUrl);
            }
        } else {
            throw new HttpException(HttpErrorCode.METHOD_TYPE_NOT_SUPPORT);
        }

        //调用Post方法
        try {
            response = httpClients.execute(httpPost);
        } catch (IOException e) {
            logger.error("Send http request IOException, error={}", e);
            throw new HttpException(HttpErrorCode.ASK_SERVICE_ERROR);
        }
        return response;
    }

    /**
     * 检查http返回的内容是否正确
     *
     * @param httpResponse http返回结果
     * @throws HttpException HttpException
     */
    private void checkHttpStatus(CloseableHttpResponse httpResponse) throws HttpException {
        if (httpResponse == null) {
            throw new HttpException(HttpErrorCode.ASK_RESPONSE_INVALID);
        }
        int status = httpResponse.getStatusLine().getStatusCode();

        logger.info("Http Status is {}", status);
        if (status != HTTP_STATUS_200 && status != HTTP_STATUS_302) {
            throw new HttpException(HttpErrorCode.ASK_RESPONSE_INVALID);
        }
    }


    /**
     * 获取服务端返回结果的字符串内容
     */
    private byte[] getResponseByte(HttpRequest request, HttpResponse httpResponse) throws Exception {
        if (null != request.getPreHandleResponse()) {
            String responseStr = request.getPreHandleResponse().preHandle(httpResponse);
            return responseStr.getBytes();
        } else {
            byte[] array = EntityUtils.toByteArray(httpResponse.getEntity());
            return array;
        }
    }

    /**
     * 获取服务端返回结果的字符串内容
     */
    private String getResponseStr(HttpRequest request, HttpResponse httpResponse) throws Exception {
        String responseStr;
        if (null != request.getPreHandleResponse()) {
            responseStr = request.getPreHandleResponse().preHandle(httpResponse);
        } else {
            byte[] array = EntityUtils.toByteArray(httpResponse.getEntity());
            responseStr = new String(array);
        }

        return responseStr;
    }

    private void buildPairByMap(List<NameValuePair> pairList, Map<String, String> paramsMap) {
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                pairList.add(pair);
            }
        }
    }

}
