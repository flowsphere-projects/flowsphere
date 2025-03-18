package com.flowsphere.agent.plugin.feign;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.utils.DynamicServerListLoadBalancerCache;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DynamicServerListLoadBalancerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        DynamicServerListLoadBalancer dynamicServerListLoadBalancer = (DynamicServerListLoadBalancer) customContextAccessor;
        DynamicServerListLoadBalancerCache.add(dynamicServerListLoadBalancer.getName(), dynamicServerListLoadBalancer);
    }

}
