package com.flowsphere.plugin.mybatis.flow;

import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.sentinel.limiter.AbstractSentinelLimiter;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;

public class MybatisLimiter extends AbstractSentinelLimiter {

    private final static MybatisLimiter INSTANT = new MybatisLimiter();

    public static MybatisLimiter getInstance() {
        return INSTANT;
    }

    @Override
    public boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig) {
        return true;
    }

}
