package com.flowsphere.agent.plugin.feign;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.flowsphere.feature.removal.ServiceNode;
import com.flowsphere.feature.removal.ServiceNodeCache;
import com.netflix.client.ClientRequest;
import com.netflix.client.IResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
public class FeignLoadBalancerInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return;
        }
        IResponse response = (IResponse) result;
        URI uri = getURI(response, allArguments);
        saveInstanceCallResult(uri, response, customContextAccessor, removalConfig);
    }

    private void saveInstanceCallResult(URI uri, IResponse response, CustomContextAccessor customContextAccessor, RemovalConfig removalConfig) {
        String key = uri.getHost() + ":" + uri.getPort();
        ServiceNode serviceNode = ServiceNodeCache.getInstanceCallResult().computeIfAbsent(key, value -> {
            ServiceNode tmpServiceNode = new ServiceNode();
            tmpServiceNode.setHost(uri.getHost());
            tmpServiceNode.setPort(uri.getPort());
            return tmpServiceNode;
        });
        if (!isSuccess(response, customContextAccessor.getCustomContext(), removalConfig)) {
            serviceNode.getRequestFailNum().incrementAndGet();
        }
        serviceNode.setLastInvokeTime(System.currentTimeMillis());
        serviceNode.getRequestNum().incrementAndGet();
        if (log.isDebugEnabled()) {
            log.info("[flowsphere] saveInstanceCallResult serviceNode={}", serviceNode);
        }
        ServiceNodeCache.saveInstanceCallResult(key, serviceNode);
    }

    private URI getURI(IResponse response, Object[] allArguments) {
        URI uri = null;
        if (Objects.isNull(response)) {
            ClientRequest request = (ClientRequest) allArguments[0];
            uri = request.getUri();
        } else {
            uri = response.getRequestedURI();
        }
        return uri;
    }

    private boolean isSuccess(IResponse response, Object context, RemovalConfig removalConfig) {
        boolean result = true;
        if (Objects.nonNull(response) && !response.isSuccess()) {
            return false;
        }
        if (Objects.nonNull(context) && context instanceof Throwable) {
            result = !isSuccess((Throwable) context, removalConfig);
        }
        return result;
    }

    private boolean isSuccess(Throwable e, RemovalConfig removalConfig) {
        if (Objects.isNull(removalConfig.getExceptions())) {
            return true;
        }
        List<String> exceptions = removalConfig.getExceptions();
        Throwable cause = e.getCause();
        if (cause == null) {
            return !exceptions.contains(e.getClass().getName());
        }
        if (cause.getCause() == null) {
            return !exceptions.contains(cause.getClass().getName());
        }
        return !exceptions.contains(cause.getCause().getClass().getName());
    }

    @Override
    public void exceptionMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Throwable e) {
        customContextAccessor.setCustomContext(e);
    }

}
