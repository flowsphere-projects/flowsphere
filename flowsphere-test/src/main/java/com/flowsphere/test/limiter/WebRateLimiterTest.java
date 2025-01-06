package com.flowsphere.test.limiter;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.flowsphere.feature.sentinel.limiter.support.WebRateLimiter;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig.HttpApiLimitConfig;
import com.flowsphere.feature.sentinel.limiter.SentinelResource;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WebRateLimiterTest {

    private static final String KEY = "/myUrl";

    @Test
    public void apiLimiterBlockExceptionTest() {
        initBlockExceptionRule();
        WebRateLimiter webRateLimiter = new WebRateLimiter();
        try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class)) {
            PluginConfig pluginConfig = new PluginConfig();
            SentinelConfig sentinelConfig = new SentinelConfig();
            HttpApiLimitConfig httpApiLimitConfig = new HttpApiLimitConfig();
            httpApiLimitConfig.setAllUrlLimitEnabled(true);
            httpApiLimitConfig.setExcludeLimitUrlList(Arrays.asList(KEY));

            sentinelConfig.setHttpApiLimitConfig(httpApiLimitConfig);
            sentinelConfig.setLimitReturnResult(Maps.newHashMap());

            pluginConfig.setSentinelConfig(sentinelConfig);
            pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);
            HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            Mockito.when(request.getRequestURI()).thenReturn(KEY);
            assertThrows(BlockException.class, () -> webRateLimiter.limit(new SentinelResource().setResourceName(request.getRequestURI()), new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    return 1;
                }

            }));
        }
    }

    private void initBlockExceptionRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY);
        // set limit qps to 20
        rule1.setCount(0);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }


    @Test
    public void httpUrlLimiterTest() {
        initRule();
        WebRateLimiter webRateLimiter = new WebRateLimiter();
        try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class)) {
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.setSentinelConfig(new SentinelConfig());
//            pluginConfig.getSentinelConfig().setHttpApiLimitEnabled(true);
            pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);
            HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            Mockito.when(request.getRequestURI()).thenReturn(KEY);
            webRateLimiter.limit(new SentinelResource().setResourceName(request.getRequestURI()), new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    return 1;
                }

            });
        }
    }


    private void initRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY);
        // set limit qps to 20
        rule1.setCount(1);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

}
