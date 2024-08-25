package com.flowsphere.agent.plugin.spring.cloud.gateway;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.agent.plugin.spring.cloud.gateway.header.SpringCloudGatewayHeaderResolver;
import com.flowsphere.agent.plugin.spring.cloud.gateway.propagator.SpringCloudGatewayPropagator;
import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SpringCloudGatewayConfig;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class LoadBalancerClientFilterInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        if (allArguments.length > 0 && allArguments[0] instanceof ServerWebExchange) {
            ServerWebExchange exchange = (ServerWebExchange) allArguments[0];
            ServerHttpRequest request = exchange.getRequest();
            InstantWeight instantWeight = getInstantWeight();
            SpringCloudGatewayPropagator propagator = new SpringCloudGatewayPropagator(request);

            HeaderResolver headerResolver = new SpringCloudGatewayHeaderResolver(request);
            propagator.inject(instantWeight, headerResolver);
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
        TagContext.remove();
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }
}
