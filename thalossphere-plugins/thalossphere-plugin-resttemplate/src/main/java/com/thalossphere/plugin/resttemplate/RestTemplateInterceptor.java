package com.thalossphere.plugin.resttemplate;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import com.thalossphere.feature.sentinel.limiter.support.SlowRatioCircuitBreakerLimiter;
import com.thalossphere.feature.sentinel.utils.SentinelContext;

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
