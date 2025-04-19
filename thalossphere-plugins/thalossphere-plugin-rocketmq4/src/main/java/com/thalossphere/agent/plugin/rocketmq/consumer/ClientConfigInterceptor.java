package com.thalossphere.agent.plugin.rocketmq.consumer;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.tag.context.TagManager;
import com.thalossphere.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ClientConfigInterceptor implements InstanceMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String systemTag = TagManager.getSystemTag();
        if (StringUtils.isNotEmpty(systemTag)) {
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(systemTag + "@" + UUID.randomUUID().toString().replace("-", ""));
        }
    }

}
