package com.thalossphere.agent.plugin.spring.mvc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.feature.sentinel.limiter.support.WebRateLimiter;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import com.thalossphere.feature.sentinel.utils.SentinelContext;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class RequestContextFilterInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) allArguments[0];
        keepDeliveringTag(httpServletRequest);
        Object result = WebRateLimiter.getInstance().limit(new SentinelResource()
                .setResourceName(httpServletRequest.getRequestURI())
                .setContextName("thalossphere_http_url_context"),
                callable);
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }


    private void keepDeliveringTag(HttpServletRequest httpServletRequest) {
        String tag = httpServletRequest.getHeader(CommonConstant.TAG);
        if (!Strings.isNullOrEmpty(tag)) {
            if (log.isDebugEnabled()) {
                log.debug("[thalossphere] spring-mvc doFilterInternal tag={}", tag);
            }
            TagContext.set(tag);
        }
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        TagContext.remove();
        SentinelContext.remove();
    }

    @SneakyThrows
    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        if (e instanceof BlockException) {
            SentinelContext.set((BlockException) e);
        }
    }


}
