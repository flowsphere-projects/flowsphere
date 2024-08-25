package com.flowsphere.test.plugin.springmvc;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.flowsphere.agent.plugin.spring.mvc.flow.FlowControlExecutor;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class FlowControlExecutorTest {

    private static final String KEY = "/myUrl";

    private static final String KEY1 = "myAppName";

    @BeforeEach
    public void before() {
        initFlowRule();
        initSystemRule();
    }

    @Test
    public void executeTest() throws BlockException {

        try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class)) {
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.setSentinelConfig(new SentinelConfig());
            pluginConfig.getSentinelConfig().setResourceLimitEnabled(true);
            pluginConfig.getSentinelConfig().setRuleKey(KEY1);
            pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);

            HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            Mockito.when(request.getRequestURI()).thenReturn(KEY);
            FlowControlExecutor.execute(new SentinelResource().setResourceName(request.getRequestURI()), new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    return 1;
                }

            });

        }

    }

    private void initFlowRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(KEY);
        // set limit qps to 20
        rule1.setCount(50);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

    private void initSystemRule() {
        List<SystemRule> rules = new ArrayList<SystemRule>();
        SystemRule rule = new SystemRule();
        // max load is 3
        rule.setHighestSystemLoad(3.0);
        // max cpu usage is 60%
        rule.setHighestCpuUsage(0.6);
        // max avg rt of all request is 10 ms
        rule.setAvgRt(10);
        // max total qps is 20
        rule.setQps(20);
        // max parallel working thread is 10
        rule.setMaxThread(10);

        rules.add(rule);
        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }

}
