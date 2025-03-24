package com.thalossphere.feature.sentinel.datasource;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.thalossphere.common.config.PluginConfigDataSource;
import com.thalossphere.common.utils.JacksonUtils;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;

import java.util.List;
import java.util.Properties;


public class SentinelNacosDataSourceRuleLoader extends AbstractSentinelDataSourceRuleLoader {


    private Properties properties;

    private String groupId;

    private String dataId;

    @Override
    public void init(PluginConfigDataSource pluginConfigDataSource) {
        properties = pluginConfigDataSource.getPros();

        PluginConfig pluginConfig = PluginConfigCache.get();
        dataId = pluginConfig.getSentinelConfig().getDataId();
        groupId = pluginConfig.getSentinelConfig().getGroupId();
    }

    @Override
    public ReadableDataSource<String, List<FlowRule>> getFlowRuleDataSource() {
        return new NacosDataSource<>(properties, groupId, dataId, source ->
                JacksonUtils.toList(source, FlowRule.class));
    }

    @Override
    public ReadableDataSource<String, List<DegradeRule>> getDegradeRuleDataSource() {
        return new NacosDataSource<>(properties, groupId, dataId, source ->
                JacksonUtils.toList(source, DegradeRule.class));
    }

    @Override
    public ReadableDataSource<String, List<AuthorityRule>> getAuthorityRuleDataSource() {
        return new NacosDataSource<>(properties, groupId, dataId, source ->
                JacksonUtils.toList(source, AuthorityRule.class));
    }

    @Override
    public ReadableDataSource<String, List<SystemRule>> getSystemRuleDataSource() {
        return new NacosDataSource<>(properties, groupId, dataId, source ->
                JacksonUtils.toList(source, SystemRule.class));
    }

    @Override
    public ReadableDataSource<String, List<ParamFlowRule>> getParamFlowRuleDataSource() {
        return new NacosDataSource<>(properties, groupId, dataId, source ->
                JacksonUtils.toList(source, ParamFlowRule.class));
    }

}
