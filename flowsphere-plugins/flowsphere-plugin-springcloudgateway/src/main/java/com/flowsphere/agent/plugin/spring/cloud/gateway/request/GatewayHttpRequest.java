package com.flowsphere.agent.plugin.spring.cloud.gateway.request;

import com.flowsphere.common.request.HttpRequest;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

public class GatewayHttpRequest implements HttpRequest {

    private final ServerHttpRequest serverHttpRequest;

    public GatewayHttpRequest(ServerHttpRequest serverHttpRequest) {
        this.serverHttpRequest = serverHttpRequest;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        serverHttpRequest.getHeaders().forEach((key, value) -> headersMap.put(key, String.join(",", value)));
        return headersMap;
    }

    @Override
    public Map<String, String> getCookies() {
        Map<String, String> cookiesMap = new HashMap<>();
        MultiValueMap<String, HttpCookie> cookies = serverHttpRequest.getCookies();
        cookies.forEach((key, value) -> cookiesMap.put(key, value.get(0).getValue()));
        return cookiesMap;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> paramsMap = new HashMap<>();
        MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
        queryParams.forEach((key, value) -> paramsMap.put(key, String.join(",", value)));
        return paramsMap;
    }

}
