package com.flowsphere.test.plugin.elasticjob;

import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.plugin.elastic.job.ShardingServiceInterceptor;
import com.flowsphere.common.utils.NetUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.ElasticJobConfig;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class ShardingServiceInterceptorTest {


    @Test
    public void beforeInstantMethodInterceptorResultIsFalseTest() {
        InstantMethodInterceptorResult instantMethodInterceptorResult = execute("127.0.0.1", "192.168.0.1", true, null);
        Assertions.assertTrue(!instantMethodInterceptorResult.isContinue());
    }

    @Test
    public void beforeInstantMethodInterceptorResultIsTrueTest() {
        InstantMethodInterceptorResult instantMethodInterceptorResult = execute("127.0.0.1", "127.0.0.1", true, Arrays.asList(1, 2, 3));
        Assertions.assertTrue(instantMethodInterceptorResult.isContinue());
    }


    private InstantMethodInterceptorResult execute(String localIp, String elasticJobConfigIp, boolean grayEnabled, Object callResult) {
        ShardingServiceInterceptor interceptor = new ShardingServiceInterceptor();
        try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class);
             MockedStatic<NetUtils> netUtilsMockedStatic = Mockito.mockStatic(NetUtils.class)) {
            netUtilsMockedStatic.when(() -> NetUtils.getIpAddress()).thenReturn(localIp);
            PluginConfig pluginConfig = new PluginConfig();
            ElasticJobConfig elasticJobConfig = new ElasticJobConfig();
            elasticJobConfig.setGrayEnabled(grayEnabled);
            elasticJobConfig.setIp(elasticJobConfigIp);
            pluginConfig.setElasticJobConfig(elasticJobConfig);
            pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);
            InstantMethodInterceptorResult instantMethodInterceptorResult = new InstantMethodInterceptorResult();
            interceptor.beforeMethod(null, new Object[]{}, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return callResult;
                }
            }, null, instantMethodInterceptorResult);
            return instantMethodInterceptorResult;
        } catch (Exception e) {
            throw e;
        }
    }

}
