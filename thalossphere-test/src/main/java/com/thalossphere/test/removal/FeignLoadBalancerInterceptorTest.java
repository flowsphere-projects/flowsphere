package com.thalossphere.test.removal;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.plugin.feign.FeignLoadBalancerInterceptor;
import com.thalossphere.common.utils.JacksonUtils;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RemovalConfig;
import com.thalossphere.feature.removal.ServiceNode;
import com.thalossphere.feature.removal.ServiceNodeCache;
import com.google.common.collect.Lists;
import com.netflix.client.ClientException;
import com.netflix.client.ClientRequest;
import com.netflix.client.IResponse;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class FeignLoadBalancerInterceptorTest {

    @BeforeEach
    public void initRemovalConfig() {
        PluginConfig pluginConfig = new PluginConfig();
        RemovalConfig removalConfig = new RemovalConfig();
        removalConfig.setApplicationName("service-b");
        removalConfig.setMinInstanceNum(1);
        removalConfig.setErrorRate(1.0D);
        removalConfig.setRecoveryTime(10000L);
        removalConfig.setScaleUpLimit(1.0D);
        removalConfig.setWindowsTime(10);
        removalConfig.setExceptions(Lists.newArrayList("java.lang.RuntimeException"));
        pluginConfig.setRemovalConfig(removalConfig);
        PluginConfigCache.put(pluginConfig);
    }

    @Test
    public void afterExceptionTest() {
        FeignLoadBalancerInterceptor interceptor = new FeignLoadBalancerInterceptor();
        URI uri = getUri();
        ClientRequest clientRequest = new ClientRequest(uri);
        Object[] params = new Object[]{clientRequest};
        interceptor.afterMethod(getExceptionCustomContextAccessor(), params, null, null, null);
        checkResult(1);
    }

    @Test
    public void afterSuccessResponseTest() {
        FeignLoadBalancerInterceptor interceptor = new FeignLoadBalancerInterceptor();
        URI uri = getUri();
        ClientRequest clientRequest = new ClientRequest(uri);
        Object[] params = new Object[]{clientRequest};
        interceptor.afterMethod(getExceptionCustomContextAccessor(), params, null, null, getSuccessIResponse(uri));
        checkResult(0);
    }

    @Test
    public void afterErrorResponseTest() {
        FeignLoadBalancerInterceptor interceptor = new FeignLoadBalancerInterceptor();
        URI uri = getUri();
        ClientRequest clientRequest = new ClientRequest(uri);
        Object[] params = new Object[]{clientRequest};
        interceptor.afterMethod(getExceptionCustomContextAccessor(), params, null, null, getErrorIResponse(uri));
        checkResult(1);
    }

    private void checkResult(int failNum) {
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(Objects.nonNull(instanceCallResult));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(instanceCallResult.values()));
        instanceCallResult.values().forEach(node -> {
            Assertions.assertTrue(node.getRequestFailNum().get() == failNum);
        });
    }

    private URI getUri() {
        return URI.create("http://127.0.0.1:8080");
    }

    private CustomContextAccessor getExceptionCustomContextAccessor() {
        return new CustomContextAccessor() {
            @Override
            public Object getCustomContext() {
                return new IllegalArgumentException();
            }

            @Override
            public void setCustomContext(Object value) {
            }
        };
    }

    private IResponse getErrorIResponse(URI uri) {
        return new IResponse() {
            @Override
            public Object getPayload() throws ClientException {
                return null;
            }

            @Override
            public boolean hasPayload() {
                return false;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public URI getRequestedURI() {
                return uri;
            }

            @Override
            public Map<String, ?> getHeaders() {
                return null;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    private IResponse getSuccessIResponse(URI uri) {
        return new IResponse() {
            @Override
            public Object getPayload() throws ClientException {
                return null;
            }

            @Override
            public boolean hasPayload() {
                return false;
            }

            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public URI getRequestedURI() {
                return uri;
            }

            @Override
            public Map<String, ?> getHeaders() {
                return null;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

}
