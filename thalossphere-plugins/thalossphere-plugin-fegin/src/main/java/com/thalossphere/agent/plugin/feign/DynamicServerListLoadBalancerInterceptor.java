package com.thalossphere.agent.plugin.feign;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.utils.DynamicServerListLoadBalancerCache;
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
