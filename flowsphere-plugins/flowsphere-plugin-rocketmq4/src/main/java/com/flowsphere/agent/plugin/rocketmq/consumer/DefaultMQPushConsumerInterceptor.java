package com.flowsphere.agent.plugin.rocketmq.consumer;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DefaultMQPushConsumerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        AllocateMessageQueueGray allocateMessageQueueGray = new AllocateMessageQueueGray();
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(allocateMessageQueueGray);
    }

}
