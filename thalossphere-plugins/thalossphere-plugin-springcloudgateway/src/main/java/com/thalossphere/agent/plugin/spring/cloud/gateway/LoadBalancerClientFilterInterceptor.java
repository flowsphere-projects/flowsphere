package com.thalossphere.agent.plugin.spring.cloud.gateway;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.agent.plugin.spring.cloud.gateway.propagator.GatewayPropagator;
import com.thalossphere.agent.plugin.spring.cloud.gateway.request.GatewayHttpRequest;
import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.SimpleAttributeResolver;
import com.thalossphere.common.request.SimpleRequestResolver;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.SpringCloudGatewayConfig;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class LoadBalancerClientFilterInterceptor implements InstanceMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        if (allArguments.length > 0 && allArguments[0] instanceof ServerWebExchange) {
            ServerWebExchange exchange = (ServerWebExchange) allArguments[0];
            ServerHttpRequest request = exchange.getRequest();
            InstantWeight instantWeight = getInstantWeight();
            GatewayPropagator propagator = new GatewayPropagator(request);
            GatewayHttpRequest gatewayHttpRequest = new GatewayHttpRequest(request);
            SimpleRequestResolver simpleRequestResolver = new SimpleRequestResolver(gatewayHttpRequest);
            propagator.inject(instantWeight, new SimpleAttributeResolver(simpleRequestResolver));
        }
    }

    private InstantWeight getInstantWeight() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        SpringCloudGatewayConfig springCloudGatewayConfig = pluginConfig.getSpringCloudGatewayConfig();
        if (Objects.isNull(springCloudGatewayConfig)) {
            return null;
        }
        InstantWeight instantWeight = new InstantWeight();
        instantWeight.setUserWeight(springCloudGatewayConfig.getUserWeight());
        instantWeight.setRegionWeight(springCloudGatewayConfig.getRegionWeight());
        return instantWeight;
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }
}
