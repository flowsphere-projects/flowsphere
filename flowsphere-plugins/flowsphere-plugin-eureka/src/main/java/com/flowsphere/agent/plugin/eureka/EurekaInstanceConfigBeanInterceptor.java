package com.flowsphere.agent.plugin.eureka;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagManager;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

public class EurekaInstanceConfigBeanInterceptor implements InstantMethodInterceptor {

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
