package com.thalossphere.agent.plugin.rocketmq.consumer;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class DefaultMQPushConsumerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        AllocateMessageQueueGray allocateMessageQueueGray = new AllocateMessageQueueGray();
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(allocateMessageQueueGray);
    }

}
