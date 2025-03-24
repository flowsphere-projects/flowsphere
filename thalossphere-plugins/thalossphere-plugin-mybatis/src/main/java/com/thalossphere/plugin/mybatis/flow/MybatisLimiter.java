package com.thalossphere.plugin.mybatis.flow;

import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.SentinelConfig;
import com.thalossphere.feature.sentinel.limiter.AbstractSentinelLimiter;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;

import java.util.Objects;

public class MybatisLimiter extends AbstractSentinelLimiter {

    private final static MybatisLimiter INSTANT = new MybatisLimiter();

    public static MybatisLimiter getInstance() {
        return INSTANT;
    }

    @Override
    public boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig) {
        SentinelConfig.MybatisApiLimitConfig mybatisApiLimitConfig = pluginConfig.getSentinelConfig().getMybatisApiLimitConfig();
        if (Objects.isNull(mybatisApiLimitConfig)) {
            return false;
        }
        return mybatisApiLimitConfig.isAllMethodLimitEnabled() || mybatisApiLimitConfig.getExcludeLimitMethodList().stream()
                .anyMatch(method -> method.equals(sentinelResource.getResourceName()));
    }

}
