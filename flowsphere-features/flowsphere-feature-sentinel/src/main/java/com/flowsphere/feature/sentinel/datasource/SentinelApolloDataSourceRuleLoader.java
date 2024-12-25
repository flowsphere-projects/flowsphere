package com.flowsphere.feature.sentinel.datasource;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.flowsphere.common.config.PluginConfigDataSource;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;

import java.util.List;

public class SentinelApolloDataSourceRuleLoader extends AbstractSentinelDataSourceRuleLoader {

    private String namespace;

    private String ruleKey;

    @Override
    public void init(PluginConfigDataSource pluginConfigDataSource) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        namespace = pluginConfig.getSentinelConfig().getNamespace();
        ruleKey = pluginConfig.getSentinelConfig().getRuleKey();
    }

    @Override
    public ReadableDataSource<String, List<FlowRule>> getFlowRuleDataSource() {
        return new ApolloDataSource<>(namespace, ruleKey, "",
                source -> JacksonUtils.toList(source, FlowRule.class));
    }

    @Override
    public ReadableDataSource<String, List<DegradeRule>> getDegradeRuleDataSource() {
        return new ApolloDataSource<>(namespace, ruleKey, "",
                source -> JacksonUtils.toList(source, DegradeRule.class));
    }

    @Override
    public ReadableDataSource<String, List<AuthorityRule>> getAuthorityRuleDataSource() {
        return new ApolloDataSource<>(namespace, ruleKey, "",
                source -> JacksonUtils.toList(source, AuthorityRule.class));
    }

    @Override
    public ReadableDataSource<String, List<SystemRule>> getSystemRuleDataSource() {
        return new ApolloDataSource<>(namespace, ruleKey, "",
                source -> JacksonUtils.toList(source, SystemRule.class));
    }

    @Override
    public ReadableDataSource<String, List<ParamFlowRule>> getParamFlowRuleDataSource() {
        return new ApolloDataSource<>(namespace, ruleKey, "",
                source -> JacksonUtils.toList(source, ParamFlowRule.class));
    }

}
