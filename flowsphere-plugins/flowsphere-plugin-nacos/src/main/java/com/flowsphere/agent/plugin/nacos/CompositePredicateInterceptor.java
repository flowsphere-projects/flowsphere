package com.flowsphere.agent.plugin.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.utils.StringUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.flowsphere.feature.removal.Instance;
import com.flowsphere.feature.removal.InstanceCallResultCache;
import com.netflix.loadbalancer.Server;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

public class CompositePredicateInterceptor implements InstantMethodInterceptor {

    private final static String SERVER_ADDR = "spring.cloud.nacos.discovery.server-addr";

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        String serverAddr = Env.get(SERVER_ADDR);
        if (StringUtils.isNotEmpty(serverAddr)) {
            Object serverListObj = allArguments[0];
            List<Server> servers = (List<Server>) serverListObj;
            List<Server> result = new ArrayList<>();
            for (Server server : servers) {
                NacosServerPredicate nacosServerPredicate = new NacosServerPredicate();
                if (server instanceof NacosServer) {
                    if (nacosServerPredicate.test((NacosServer) server)) {
                        result.add(server);
                    }
                }
            }
            result = removal(result);
            instantMethodInterceptorResult.setContinue(false);
            //兜底路由
            if (CollectionUtils.isEmpty(result)) {
                instantMethodInterceptorResult.setResult(servers);
                return;
            }
            instantMethodInterceptorResult.setResult(result);
        }
    }

    private List<Server> removal(List<Server> instanceList) {
        Map<String, Instance> instanceCallResult = InstanceCallResultCache.getInstanceCallResult();
        if (instanceCallResult.isEmpty()) {
            return instanceList;
        }
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return instanceList;
        }

        double canRemovalNum = getCanRemovalNum(instanceList, removalConfig);

        List<Server> result = new ArrayList<>();
        for (Server server : instanceList) {
            Instance instance = instanceCallResult.get(server.getHostPort());
            if (Objects.isNull(instance)) {
                continue;
            }
            if (instance.getErrorRate() >= removalConfig.getErrorRate() && canRemovalNum >= 1
                    && instance.getRemovalStatus().compareAndSet(false, true)) {
                instance.setRemovalTime(System.currentTimeMillis());
                instance.setRecoveryTime(System.currentTimeMillis() + removalConfig.getRecoveryTime());
                //TODO 通知服务端，需要下线某个服务
            } else {
                result.add(server);
            }
        }
        return result;
    }


    public double getCanRemovalNum(List<Server> instanceList, RemovalConfig removalConfig) {
        int removalCount = instanceList.size();
        return Math.min(instanceList.size() * removalConfig.getScaleUpLimit() - removalCount,
                instanceList.size() - removalCount - removalConfig.getMinInstanceNum());
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
    }


    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {

    }

}
