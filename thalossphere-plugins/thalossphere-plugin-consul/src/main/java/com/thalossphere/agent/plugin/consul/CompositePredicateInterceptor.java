package com.thalossphere.agent.plugin.consul;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.env.Env;
import com.thalossphere.common.utils.StringUtils;
import com.netflix.loadbalancer.Server;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

public class CompositePredicateInterceptor implements InstantMethodInterceptor {

    private final static String SERVER_ADDR = "spring.cloud.consul.host";

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String serverAddr = Env.get(SERVER_ADDR);
        if (StringUtils.isNotEmpty(serverAddr)) {
            Object serverListObj = allArguments[0];
            List<Server> servers = (List<Server>) serverListObj;
            servers = ConsulSelector.getInstance().selectInstances(servers);
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(servers);
        }
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }


    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

}
