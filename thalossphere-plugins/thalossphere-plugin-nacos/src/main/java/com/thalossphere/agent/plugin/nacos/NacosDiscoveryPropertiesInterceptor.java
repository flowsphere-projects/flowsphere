package com.thalossphere.agent.plugin.nacos;


import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

public class NacosDiscoveryPropertiesInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Map<String, String> metadata = (Map<String, String>) allArguments[0];
        metadata.put(CommonConstant.SERVER_TAG, TagManager.getSystemTag());
        metadata.put(CommonConstant.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {

    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }
}
