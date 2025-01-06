package com.flowsphere.feature.sentinel.limiter.support;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.feature.sentinel.limiter.AbstractSentinelLimiter;
import com.flowsphere.feature.sentinel.limiter.SentinelResource;
import lombok.SneakyThrows;

import java.util.Objects;


public class WebRateLimiter extends AbstractSentinelLimiter {

    private final static WebRateLimiter INSTANTCE = new WebRateLimiter();

    public static WebRateLimiter getInstance() {
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
