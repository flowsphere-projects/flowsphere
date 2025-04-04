package com.thalossphere.feature.sentinel.limiter.support;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.feature.sentinel.limiter.AbstractSentinelLimiter;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import lombok.SneakyThrows;

public class SlowRatioCircuitBreakerLimiter extends AbstractSentinelLimiter {

    private static final SlowRatioCircuitBreakerLimiter INSTANT = new SlowRatioCircuitBreakerLimiter();

    public static SlowRatioCircuitBreakerLimiter getInstance() {
        return INSTANT;
    }

    @SneakyThrows
    @Override
    public Entry getEntry(SentinelResource sentinelResource) {
        return SphU.entry(sentinelResource.getResourceName());
    }

    @Override
    public boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig) {
        return pluginConfig.getSentinelConfig().isCircuitBreakerEnabled();
    }

}
