package com.thalossphere.agent.plugin.spring.mvc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.feature.sentinel.limiter.support.DynamicMachineIndicatorsLimiter;
import com.thalossphere.common.env.Env;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import com.thalossphere.feature.sentinel.utils.SentinelContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.thalossphere.common.constant.CommonConstant.SPRING_APPLICATION_NAME;

public class DynamicMachineIndicatorsLimiterInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig.getSentinelConfig()) || StringUtils.isEmpty(Env.get(SPRING_APPLICATION_NAME))) {
            return;
        }
        DynamicMachineIndicatorsLimiter.getInstance().limit(new SentinelResource().setResourceName(Env.get(SPRING_APPLICATION_NAME)), callable);
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        if (e instanceof BlockException) {
            SentinelContext.set((BlockException) e);
        }
    }
}
