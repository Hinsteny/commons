package com.github.hinsteny.commons.warp.http.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * HttpClient的单例类.
 *
 * @author Hinsteny
 * @version HttpClientsInstance: 2019-08-12 16:25 All rights reserved.$
 */
public enum HttpClientsInstance {

    /**
     * instance
     */
    INSTANCE;

    /**
     * 默认最大连接数
     */
    private static final int DEFAULT_MAX_TOTAL = 200;

    /**
     * 默认最大连接数(同一主机)
     */
    private static final int DEFAULT_MAX_PRE_TOTAL = 200;

    private HttpClient httpClient;

    /**
     * 获取枚举单例实例
     *
     * @return
     */
    public HttpClient getInstance() {
        return httpClient;
    }

    /**
     * 构造函数内进行初始化, 保证线程安全
     */
    HttpClientsInstance() {
        Registry<ConnectionSocketFactory> socketFactoryRegistry;
        try {
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(createIgnoreVerifySSL()))
                .build();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL);
            connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PRE_TOTAL);

            CloseableHttpClient httpClients = HttpClients.custom().setConnectionManager(connectionManager).build();
            httpClient = new HttpClient(httpClients);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("初始化httpclient出错...");
        }
    }

    /**
     * 创建忽略SSL校验的https客户端
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

}
