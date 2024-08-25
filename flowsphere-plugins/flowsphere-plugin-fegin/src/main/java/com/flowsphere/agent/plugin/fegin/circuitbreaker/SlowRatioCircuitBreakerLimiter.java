package com.flowsphere.agent.plugin.fegin.circuitbreaker;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.sentinel.limiter.AbstractSentinelLimiter;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import feign.Request;
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
