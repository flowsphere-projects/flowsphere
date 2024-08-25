package com.flowsphere.agent.plugin.rocketmq.consumer;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ClientConfigInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String systemTag = TagManager.getSystemTag();
        if (StringUtils.isNotEmpty(systemTag)) {
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(systemTag + "@" + UUID.randomUUID().toString().replace("-", ""));
        }
    }

}
