package com.le.jr.am.profit.service.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * HTTP工具类
 *
 * @author jiazhipeng
 * @version 1.0
 * @date 2016-07-20
 */
public class HttpHelper {

    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static RestTemplate restTemplate;

    private static Integer readTimeOut = 10 * 1000;
    private static Integer connectTimeOut = 10 * 1000;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(readTimeOut);
        requestFactory.setConnectTimeout(connectTimeOut);
        restTemplate = new RestTemplate(requestFactory);
    }

    private HttpHelper() {
    }

    public static <T> T postRequestResult(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        return restTemplate.postForObject(url, params, responseType);
    }

    public static <T> T postRequestResult(String url, Map<String, String> params, Class<T> responseType) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> m : params.entrySet()) {
            multiValueMap.set(m.getKey(), m.getValue());
        }
        return postRequestResult(url, multiValueMap, responseType);
    }

    public static <T> T getRequestResult(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }


    public static <T> T getRequestResult(String url, Class<T> responseType, Object... urlVariables) {
        return restTemplate.getForObject(url, responseType, urlVariables);
    }
}
