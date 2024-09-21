package com.flowsphere.plugin.resttemplate;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import com.flowsphere.extension.sentinel.limiter.support.SlowRatioCircuitBreakerLimiter;
import com.flowsphere.extension.sentinel.utils.SentinelContext;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.Callable;

public class RestTemplateInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        URI url = (URI) allArguments[0];
        Object result = SlowRatioCircuitBreakerLimiter.getInstance().limit(
                new SentinelResource().setResourceName(url.getPath()), callable);
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }


    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        if (e instanceof BlockException) {
            SentinelContext.set((BlockException) e);
        }
    }

}
