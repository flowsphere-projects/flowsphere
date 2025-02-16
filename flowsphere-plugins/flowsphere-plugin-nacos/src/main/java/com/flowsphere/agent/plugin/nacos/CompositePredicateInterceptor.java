package com.flowsphere.agent.plugin.nacos;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.utils.StringUtils;
import com.netflix.loadbalancer.Server;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

public class CompositePredicateInterceptor implements InstantMethodInterceptor {

    private final static String SERVER_ADDR = "spring.cloud.nacos.discovery.server-addr";

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String serverAddr = Env.get(SERVER_ADDR);
        if (StringUtils.isNotEmpty(serverAddr)) {
            Object serverListObj = allArguments[0];
            List<Server> servers = (List<Server>) serverListObj;
            servers = NacosSelector.getInstance().selectInstances(servers);
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
