package com.flowsphere.agent.plugin.spring.mvc.flow;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.extension.sentinel.limiter.AbstractSentinelLimiter;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;
import lombok.SneakyThrows;

import java.util.Objects;


public class RateLimiter extends AbstractSentinelLimiter {

    private final static RateLimiter INSTANTCE = new RateLimiter();

    public static RateLimiter getInstance() {
        return INSTANTCE;
    }


    @SneakyThrows
    @Override
    public Entry getEntry(SentinelResource sentinelResource) {
        return SphU.entry(sentinelResource.getResourceName(), ResourceTypeConstants.COMMON_WEB, EntryType.IN);
    }

    @Override
    public boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig) {
        SentinelConfig.HttpApiLimitConfig httpApiLimitConfig = pluginConfig.getSentinelConfig().getHttpApiLimitConfig();
        if (Objects.isNull(httpApiLimitConfig)) {
            return false;
        }
        return httpApiLimitConfig.isAllUrlLimitEnabled() || httpApiLimitConfig.getExcludeLimitUrlList().stream()
                .anyMatch(url -> url.equals(sentinelResource.getResourceName()));
    }

}
