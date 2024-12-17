package com.flowsphere.test.limiter;

import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.flowsphere.features.sentinel.limiter.support.DynamicMachineIndicatorsLimiter;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.features.sentinel.limiter.SentinelResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DynamicMachineIndicatorsLimiterTest {

    private static final String KEY = "/myUrl";

    @BeforeEach
    public void before() {
        List<SystemRule> rules = new ArrayList<SystemRule>();
        SystemRule rule = new SystemRule();
        // max load is 3
        rule.setHighestSystemLoad(3.0);
        // max cpu usage is 60%
        rule.setHighestCpuUsage(0.01);
        // max avg rt of all request is 10 ms
        rule.setAvgRt(10);
        // max total qps is 20
        rule.setQps(20);
        // max parallel working thread is 10
        rule.setMaxThread(10);

        rules.add(rule);
        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }


    @Test
    public void limiterTest() throws InterruptedException {
        DynamicMachineIndicatorsLimiter limiter = new DynamicMachineIndicatorsLimiter();
        assertThrows(SystemBlockException.class, () -> {
            try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class)) {
                PluginConfig pluginConfig = new PluginConfig();
                pluginConfig.setSentinelConfig(new SentinelConfig());
                pluginConfig.getSentinelConfig().setResourceLimitEnabled(true);
                pluginConfig.getSentinelConfig().setRuleKey("myAppName");
                pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);
                HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
                Mockito.when(request.getRequestURI()).thenReturn(KEY);
                limiter.limit(new SentinelResource().setResourceName(request.getRequestURI()), new Callable<Object>() {

                    @Override
                    public Object call() throws Exception {
                        return 1;
                    }

                });
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    // ignore
                    e.printStackTrace();
                }
            } catch (Exception e) {
                throw e;
            }
        });
    }


}
