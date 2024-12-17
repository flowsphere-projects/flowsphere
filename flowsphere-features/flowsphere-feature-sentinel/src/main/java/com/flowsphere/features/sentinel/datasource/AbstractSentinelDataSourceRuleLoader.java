package com.flowsphere.features.sentinel.datasource;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.flowsphere.common.config.PluginConfigDataSource;

import java.util.List;

public abstract class AbstractSentinelDataSourceRuleLoader implements SentinelDataSourceRuleLoader {

    @Override
    public void load(PluginConfigDataSource pluginConfigDataSource) {
        init(pluginConfigDataSource);

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = getFlowRuleDataSource();
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = getDegradeRuleDataSource();
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = getAuthorityRuleDataSource();
        AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());

        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = getSystemRuleDataSource();
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = getParamFlowRuleDataSource();
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

    }

    public abstract void init(PluginConfigDataSource pluginConfigDataSource);

    public abstract ReadableDataSource<String, List<FlowRule>> getFlowRuleDataSource();

    public abstract ReadableDataSource<String, List<DegradeRule>> getDegradeRuleDataSource();

    public abstract ReadableDataSource<String, List<AuthorityRule>> getAuthorityRuleDataSource();

    public abstract ReadableDataSource<String, List<SystemRule>> getSystemRuleDataSource();

    public abstract ReadableDataSource<String, List<ParamFlowRule>> getParamFlowRuleDataSource();

}
