package com.flowsphere.agent.plugin.eureka;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.common.utils.StringUtils;
import com.google.common.base.Strings;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            List<Server> resultList = new ArrayList<>();
            for (Server server : serverList) {
                if (server instanceof DiscoveryEnabledServer) {
                    DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
                    InstanceInfo instanceInfo = discoveryEnabledServer.getInstanceInfo();
                    Map<String, String> metadata = instanceInfo.getMetadata();
                    String serverTag = metadata.get(CommonConstant.TAG_KEY);
                    String tag = TagContext.get();
                    if (log.isDebugEnabled()) {
                        log.debug("[flowsphere] BaseLoadBalancerInterceptor eureka tag={}", tag);
                    }
                    if (!Strings.isNullOrEmpty(tag) && tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
                        resultList.add(server);
                    }
                }
            }
            if (CollectionUtils.isEmpty(resultList)) {
                instantMethodInterceptorResult.setResult(serverList);
            } else {
                instantMethodInterceptorResult.setResult(resultList);
            }
            instantMethodInterceptorResult.setContinue(false);
        }
    }

}
