package com.thalossphere.agent.plugin.zuul;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.agent.plugin.zuul.propagator.ZuulPropagator;
import com.thalossphere.agent.plugin.zuul.request.ZuulHttpRequest;
import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.SimpleAttributeResolver;
import com.thalossphere.common.request.SimpleRequestResolver;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.ZuulConfig;
import com.netflix.zuul.context.RequestContext;
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
        RequestContext context = RequestContext.getCurrentContext();
        ZuulHttpRequest zuulHttpRequest = new ZuulHttpRequest(context.getRequest());
        SimpleRequestResolver simpleRequestResolver = new SimpleRequestResolver(zuulHttpRequest);
        zuulPropagator.inject(instantWeight, new SimpleAttributeResolver(simpleRequestResolver));
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
