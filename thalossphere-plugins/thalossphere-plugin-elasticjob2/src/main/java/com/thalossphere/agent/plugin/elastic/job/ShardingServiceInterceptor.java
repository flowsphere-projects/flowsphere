package com.thalossphere.agent.plugin.elastic.job;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.utils.NetUtils;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
public class ShardingServiceInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        try {
            String localIp = NetUtils.getIpAddress();
            boolean elasticJobGrayEnabled = PluginConfigCache.get().getElasticJobConfig().isGrayEnabled();
            String executeIp = PluginConfigCache.get().getElasticJobConfig().getIp();
            if (elasticJobGrayEnabled && localIp.equals(executeIp)) {
                Object call = callable.call();
                if (Objects.nonNull(call) && ((List<Integer>) call).size() > 1) {
                    instantMethodInterceptorResult.setResult(call);
                    return;
                }
                instantMethodInterceptorResult.setResult(Arrays.asList(0));
            } else {
                instantMethodInterceptorResult.setResult(Collections.emptyList());
            }
            instantMethodInterceptorResult.setContinue(false);
        } catch (Exception e) {
            log.error("[thalossphere] elastic job interceptor execute error", e);
        }
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

}
