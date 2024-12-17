package com.flowsphere.agent.plugin.feign;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.features.sentinel.limiter.SentinelResource;
import com.flowsphere.features.sentinel.limiter.support.SlowRatioCircuitBreakerLimiter;
import com.flowsphere.feature.removal.Instance;
import com.flowsphere.feature.removal.InstanceCallResultCache;
import com.google.common.base.Strings;
import feign.Request;
import feign.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
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

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        //解析状态码
        Response response = (Response) result;
        if (Objects.isNull(response)) {
            return;
        }
        URI uri = new URI(response.request().url());
        Instance instantInfo = new Instance();
        instantInfo.getRequestNum().incrementAndGet();
        instantInfo.setHost(uri.getHost());
        instantInfo.setPort(uri.getPort());
        instantInfo.setLastInvokeTime(System.currentTimeMillis());
        if (isSuccess(response, (Throwable) customContextAccessor.getCustomContext())) {
            instantInfo.getRequestFailNum().incrementAndGet();
        }
        InstanceCallResultCache.saveInstanceCallResult(instantInfo);
    }


    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        customContextAccessor.setCustomContext(e);
    }

    private boolean isSuccess(Response response, Throwable e) {
        if (Objects.nonNull(response)) {
            return isSuccess(response);
        }
        if (Objects.nonNull(e)) {
            return isSuccess(e);
        }
        return true;
    }

    private boolean isSuccess(Response response) {
        return response.status() == 200;
    }

    private boolean isSuccess(Throwable e) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig.getRemovalConfig()) || Objects.isNull(pluginConfig.getRemovalConfig().getExceptions())) {
            return true;
        }
        List<String> exceptions = pluginConfig.getRemovalConfig().getExceptions();
        Throwable cause = e.getCause();

        if (cause == null) {
            return !exceptions.contains(e.getClass().getName());
        }
        if (cause.getCause() == null) {
            return !exceptions.contains(cause.getClass().getName());
        }
        return !exceptions.contains(cause.getCause().getClass().getName());
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
