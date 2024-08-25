package com.flowsphere.agent.plugin.zuul;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.agent.plugin.zuul.header.ZuulHeaderResolver;
import com.flowsphere.agent.plugin.zuul.propagator.ZuulPropagator;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.ZuulConfig;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class RibbonRoutingFilterInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        RibbonCommandContext commandContext = (RibbonCommandContext) allArguments[0];
        ZuulPropagator zuulPropagator = new ZuulPropagator(commandContext);
        InstantWeight instantWeight = getInstantWeight();
        ZuulHeaderResolver zuulHeaderResolver = new ZuulHeaderResolver(commandContext);
        zuulPropagator.inject(instantWeight, zuulHeaderResolver);
    }

    private InstantWeight getInstantWeight() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        ZuulConfig zuulConfig = pluginConfig.getZuulConfig();
        if (Objects.isNull(zuulConfig)) {
            return null;
        }
        InstantWeight instantWeight = new InstantWeight();
        instantWeight.setUserWeight(zuulConfig.getUserWeight());
        instantWeight.setRegionWeight(zuulConfig.getRegionWeight());
        return instantWeight;
    }

}
