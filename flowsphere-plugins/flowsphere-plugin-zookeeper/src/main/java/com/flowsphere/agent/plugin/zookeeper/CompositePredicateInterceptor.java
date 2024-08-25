package com.flowsphere.agent.plugin.zookeeper;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.utils.StringUtils;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CompositePredicateInterceptor implements InstantMethodInterceptor {

    private final static String SERVER_ADDR = "spring.cloud.zookeeper.connect-string";

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String serverAddr = Env.get(SERVER_ADDR);
        if (StringUtils.isNotEmpty(serverAddr)) {
            Object serverListObj = allArguments[0];
            List<Server> servers = (List<Server>) serverListObj;
            List<Server> result = new ArrayList<>();
            for (Server server : servers) {
                ZookeeperServerPredicate zookeeperServerPredicate = new ZookeeperServerPredicate();
                if (server instanceof ZookeeperServer) {
                    if (zookeeperServerPredicate.test((ZookeeperServer) server)) {
                        result.add(server);
                    }
                }
            }
            instantMethodInterceptorResult.setContinue(false);
            //兜底路由
            if (CollectionUtils.isEmpty(result)) {
                instantMethodInterceptorResult.setResult(servers);
                return;
            }
            instantMethodInterceptorResult.setResult(result);
        }
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }


    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

}
