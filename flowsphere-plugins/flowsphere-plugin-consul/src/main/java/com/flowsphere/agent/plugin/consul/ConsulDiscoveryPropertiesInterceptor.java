package com.flowsphere.agent.plugin.consul;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagManager;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

public class ConsulDiscoveryPropertiesInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        List<String> tags = (List<String>) callable.call();
        tags.add(CommonConstant.SERVER_TAG + "=" + TagManager.getSystemTag());
        tags.add(CommonConstant.TIMESTAMP + "=" + System.currentTimeMillis());
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(tags);
    }

}
