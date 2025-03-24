package com.thalossphere.common.request;

public interface RequestResolver {

    String getHeader(String key);

    String getCookie(String key);

    String getParameters(String key);

}
