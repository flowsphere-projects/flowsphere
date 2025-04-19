package com.thalossphere.agent.plugin.eureka;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagManager;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

public class EurekaInstanceConfigBeanInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Object callResult = callable.call();
        Map<String, String> result = (Map<String, String>) callResult;
        result.put(CommonConstant.SERVER_TAG, TagManager.getSystemTag());
        result.put(CommonConstant.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }

}
