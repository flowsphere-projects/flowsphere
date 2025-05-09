package com.thalossphere.agent.plugin.feign;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.feature.removal.ServiceNode;
import com.thalossphere.feature.removal.ServiceNodeCache;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import com.thalossphere.feature.sentinel.limiter.support.SlowRatioCircuitBreakerLimiter;
import com.google.common.base.Strings;
import com.netflix.client.ClientRequest;
import com.netflix.client.IResponse;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
public class LoadBalancerFeignClientInterceptor implements InstantMethodInterceptor {

    private static final String DECLARED_FIELD_NAME = "headers";

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Request request = (Request) allArguments[0];
        Map<String, Collection<String>> headers = resolver(request);
        headersField(request, headers);
        Object result = SlowRatioCircuitBreakerLimiter.getInstance().limit(
                new SentinelResource().setResourceName(request.url()), callable);
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }

    private void headersField(Request request, Map<String, Collection<String>> headers) {
        try {
            Field headersField = Request.class.getDeclaredField(DECLARED_FIELD_NAME);
            headersField.setAccessible(true);
            headersField.set(request, Collections.unmodifiableMap(headers));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private Map<String, Collection<String>> resolver(Request request) {
        Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
        headers.putAll(request.headers());
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[thalossphere] resolver feign tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag)) {
            List<String> ruleList = new ArrayList<String>();
            ruleList.add(tag);
            headers.put(CommonConstant.TAG, ruleList);
        }
        return headers;
    }

}
