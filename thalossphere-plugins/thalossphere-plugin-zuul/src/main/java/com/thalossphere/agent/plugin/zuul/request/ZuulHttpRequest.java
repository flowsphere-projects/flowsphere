package com.thalossphere.agent.plugin.zuul.request;

import com.thalossphere.common.request.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ZuulHttpRequest implements HttpRequest {

    private final HttpServletRequest httpServletRequest;

    public ZuulHttpRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, httpServletRequest.getHeader(headerName));
        }
        return headers;
    }

    @Override
    public Map<String, String> getCookies() {
        Map<String, String> cookiesMap = new HashMap<>();
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookiesMap;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.put(paramName, httpServletRequest.getParameter(paramName));
        }
        return params;
    }

}
