package com.flowsphere.agent.plugin.feign;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.feature.removal.ServiceNode;
import com.flowsphere.feature.removal.ServiceNodeCache;
import com.netflix.client.ClientRequest;
import com.netflix.client.IResponse;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class FeignLoadBalancerInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        IResponse response = (IResponse) result;
        URI uri = getURI(response, allArguments);
        saveInstanceCallResult(uri, response, customContextAccessor);
    }

    private void saveInstanceCallResult(URI uri, IResponse response, CustomContextAccessor customContextAccessor) {
        String key = uri.getHost() + ":" + uri.getPort();
        ServiceNode serviceNode = ServiceNodeCache.getInstanceCallResult().computeIfAbsent(key, value -> {
            ServiceNode tmpServiceNode = new ServiceNode();
            tmpServiceNode.setLastInvokeTime(System.currentTimeMillis());
            if (!isSuccess(response, customContextAccessor.getCustomContext())) {
                tmpServiceNode.getRequestFailNum().incrementAndGet();
            }
            tmpServiceNode.setHost(uri.getHost());
            tmpServiceNode.setPort(uri.getPort());
            tmpServiceNode.getRequestNum().incrementAndGet();
            return tmpServiceNode;
        });
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

    private boolean isSuccess(IResponse response, Object context) {
        if (Objects.nonNull(response)) {
            return response.isSuccess();
        }
        if (Objects.nonNull(context) && context instanceof Throwable) {
            return isSuccess((Throwable) context);
        }
        return true;
    }

    private boolean isSuccess(Throwable e) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig.getRemovalConfig()) || Objects.isNull(pluginConfig.getRemovalConfig().getExceptions())) {
            return true;
        }
        List<String> exceptions = pluginConfig.getRemovalConfig().getExceptions();
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
