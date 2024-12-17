package com.flowsphere.features.sentinel.limiter.support;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.features.sentinel.limiter.AbstractSentinelLimiter;
import com.flowsphere.features.sentinel.limiter.SentinelResource;
import lombok.SneakyThrows;

/**
 * 实现动态限流根据CPU/内存
 */
public class DynamicMachineIndicatorsLimiter extends AbstractSentinelLimiter {

    private final static DynamicMachineIndicatorsLimiter INSTANTCE = new DynamicMachineIndicatorsLimiter();

    public static DynamicMachineIndicatorsLimiter getInstance() {
        return INSTANTCE;
    }

    @Override
    public boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig) {
        return pluginConfig.getSentinelConfig().isResourceLimitEnabled();
    }

    @SneakyThrows
    @Override
    public Entry getEntry(SentinelResource sentinelResource) {
        return SphU.entry(sentinelResource.getResourceName(), EntryType.IN);
    }

}