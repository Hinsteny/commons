package com.github.hinsteny.test.commons.warp.http;

import static com.github.hinsteny.commons.warp.http.client.HttpClientsInstance.INSTANCE;

import com.github.hinsteny.commons.warp.http.exception.HttpException;
import com.github.hinsteny.commons.warp.http.request.HttpRequest;
import com.github.hinsteny.commons.warp.http.request.MethodType;
import com.github.hinsteny.commons.warp.http.request.ParamType;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

/**
 * @author Hinsteny
 * @version HttpClientTest: 2019-08-12 16:29 All rights reserved.$
 */
public class HttpClientTest {

    @Test
    public void testHttpGet() {
        String url = "https://www.github.com/Hinsteny";
        HttpRequest request = new HttpRequest(url, MethodType.GET, "");
        request.setConnectTimeout(3000);
        request.setSocketTimeout(5000);

        try {
            String html = INSTANCE.getInstance().sendBackString(request);
            System.out.println(html);
        } catch (HttpException e) {

        } catch (Exception e) {
        }
    }

    @Test
    public void testHttpGetQuery() {
        Map<String, String> body = new HashMap<>();
        body.put("user", "Hinsteny");
        body.put("q", "spring");

        String url = "https://www.github.com/search";
        HttpRequest request = new HttpRequest(url, MethodType.GET, ParamType.QUERY, body);
        request.setConnectTimeout(3000);
        request.setSocketTimeout(5000);

        try {
            String html = INSTANCE.getInstance().sendBackString(request);
            System.out.println(html);
        } catch (HttpException e) {

        } catch (Exception e) {
        }
    }

}
