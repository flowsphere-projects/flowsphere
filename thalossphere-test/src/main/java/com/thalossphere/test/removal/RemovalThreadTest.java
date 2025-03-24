package com.thalossphere.test.removal;

import com.thalossphere.common.config.OkHttpConfig;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RemovalConfig;
import com.thalossphere.feature.removal.RemovalThread;
import com.thalossphere.feature.removal.ServiceNode;
import com.thalossphere.feature.removal.ServiceNodeCache;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

public class RemovalThreadTest {

    @BeforeEach
    public void before() {
        FeignLoadBalancerInterceptorTest test = new FeignLoadBalancerInterceptorTest();
        test.initRemovalConfig();
        test.afterExceptionTest();
        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        OkHttpConfig okHttpConfig = new OkHttpConfig();
        okHttpConfig.setConnectTimeout(30L);
        okHttpConfig.setWriteTimeout(30L);
        okHttpConfig.setReadTimeout(30L);
        yamlAgentConfig.setOkHttpConfig(okHttpConfig);
        yamlAgentConfig.setServerAddr("http://localhost:8224");
        YamlAgentConfigCache.put(yamlAgentConfig);
    }

    @Test
    public void errorRateTest() {
        RemovalThread removalThread = new RemovalThread();
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        removalConfig.setRecoveryTime(100000000l);
        removalThread.run();
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(Objects.nonNull(instanceCallResult));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(instanceCallResult.values()));
        instanceCallResult.values().forEach(node -> {
            Assertions.assertTrue(node.getErrorRate() >= 1);
        });
    }

    @Test
    public void resetTest() {
        RemovalThread removalThread = new RemovalThread();
        removalThread.run();
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(Objects.nonNull(instanceCallResult));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(instanceCallResult.values()));
    }

}
