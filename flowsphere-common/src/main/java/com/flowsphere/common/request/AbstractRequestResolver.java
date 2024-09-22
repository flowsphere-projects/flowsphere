package com.flowsphere.common.request;

import java.util.Map;

public abstract class AbstractRequestResolver implements RequestResolver {

    private final HttpRequest httpRequest;

    public AbstractRequestResolver(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public String getHeader(String key) {
        Map<String, String> headers = httpRequest.getHeaders();
        return headers.get(key);
    }

    @Override
    public String getCookie(String key) {
        Map<String, String> cookies = httpRequest.getCookies();
        return cookies.get(key);
    }

    @Override
    public String getParameters(String key) {
        Map<String, String> parameters = httpRequest.getParameters();
        return parameters.get(key);
    }


}
