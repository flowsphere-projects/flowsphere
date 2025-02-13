package com.flowsphere.agent.plugin.eureka;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.utils.StringUtils;
import com.netflix.loadbalancer.Server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * com.netflix.loadbalancer.BaseLoadBalancer#getAllServers()
 */
@Slf4j
public class BaseLoadBalancerInterceptor implements InstantMethodInterceptor {

    private static final String SERVICE_URL = "eureka.client.service-url.default-zone";

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String serviceUrl = Env.get(SERVICE_URL);
        if (StringUtils.isNotEmpty(serviceUrl)) {
            Object obj = callable.call();
            List<Server> serverList = (List<Server>) obj;
            serverList = EurekaSelector.getInstance().selectInstances(serverList);
            instantMethodInterceptorResult.setResult(serverList);
            instantMethodInterceptorResult.setContinue(false);
        }
    }

}
