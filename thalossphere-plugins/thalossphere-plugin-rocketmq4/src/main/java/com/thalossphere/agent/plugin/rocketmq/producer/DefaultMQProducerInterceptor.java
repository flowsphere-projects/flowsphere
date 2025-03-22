package com.thalossphere.agent.plugin.rocketmq.producer;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagManager;
import org.apache.rocketmq.common.message.Message;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DefaultMQProducerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Message message = (Message) allArguments[0];
        message.putUserProperty(CommonConstant.TAG, TagManager.getTag());
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {

    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

}
