package com.flowsphere.agent.plugin.spring.mvc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.agent.plugin.spring.mvc.constant.SpringMvcConstant;
import com.flowsphere.agent.plugin.spring.mvc.flow.DynamicMachineIndicatorsLimiter;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DynamicMachineIndicatorsLimiterInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig.getSentinelConfig()) || StringUtils.isEmpty(Env.get("spring.application.name"))) {
            return;
        }
        DynamicMachineIndicatorsLimiter.getInstance().limit(new SentinelResource().setResourceName(Env.get("spring.application.name")), callable);
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        if (e instanceof BlockException) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) allArguments[0];
            httpServletRequest.setAttribute(SpringMvcConstant.SENTINEL_LIMIT_KEY, true);
        }
    }
}
