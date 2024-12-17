package com.flowsphere.agent.plugin.spring.mvc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.features.sentinel.limiter.support.DynamicMachineIndicatorsLimiter;
import com.flowsphere.common.env.Env;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.features.sentinel.limiter.SentinelResource;
import com.flowsphere.features.sentinel.utils.SentinelContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.flowsphere.common.constant.CommonConstant.SPRING_APPLICATION_NAME;

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
