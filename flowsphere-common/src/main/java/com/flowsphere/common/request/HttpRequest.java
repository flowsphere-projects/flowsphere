package com.flowsphere.common.request;

import java.util.Map;

public interface HttpRequest {

    Map<String, String> getHeaders();

    Map<String, String> getCookies();

    Map<String, String> getParameters();
}
