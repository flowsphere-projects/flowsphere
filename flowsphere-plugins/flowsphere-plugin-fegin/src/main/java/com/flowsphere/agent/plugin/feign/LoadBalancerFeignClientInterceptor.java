package com.flowsphere.agent.plugin.feign;


import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import com.flowsphere.extension.sentinel.limiter.support.SlowRatioCircuitBreakerLimiter;
import com.google.common.base.Strings;
import feign.Request;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
public class LoadBalancerFeignClientInterceptor implements InstantMethodInterceptor {

    private static final String METHOD_NAME = "execute";

    private static final String DECLARED_FIELD_NAME = "headers";


    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        if (method.getName().equals(METHOD_NAME)) {
            if (allArguments.length > 0 && allArguments[0] instanceof Request) {
                Request request = (Request) allArguments[0];
                Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
                headers.putAll(request.headers());
                resolver(headers);
                try {
                    Field headersField = Request.class.getDeclaredField(DECLARED_FIELD_NAME);
                    headersField.setAccessible(true);
                    headersField.set(request, Collections.unmodifiableMap(headers));
                } catch (Exception e) {
                    log.error("", e);
                }
                Object result = SlowRatioCircuitBreakerLimiter.getInstance().limit(
                        new SentinelResource().setResourceName(request.url()), callable);
                instantMethodInterceptorResult.setContinue(false);
                instantMethodInterceptorResult.setResult(result);
            }
        }
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

    private void resolver(Map<String, Collection<String>> headers) {
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] FeignInstantMethodInterceptor feign tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag)) {
            List<String> ruleList = new ArrayList<String>();
            ruleList.add(tag);
            headers.put(CommonConstant.TAG, ruleList);
        }
    }

}
