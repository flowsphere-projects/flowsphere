package com.flowsphere.feature.sentinel.limiter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.nacos.common.utils.StringUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

@Slf4j
public abstract class AbstractSentinelLimiter implements SentinelLimiter {

    private static final String DEFAULT_ORIGIN = "";

    @SneakyThrows
    @Override
    public Object limit(SentinelResource sentinelResource, Callable<?> callable) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig) || Objects.isNull(pluginConfig.getSentinelConfig())) {
            return callable.call();
        }
        boolean isNeed = needLimit(sentinelResource, pluginConfig);
        if (!isNeed) {
            return callable.call();
        }
        return doInLimit(sentinelResource, callable);
    }

    public abstract boolean needLimit(SentinelResource sentinelResource, PluginConfig pluginConfig);

    @SneakyThrows
    private Object doInLimit(SentinelResource sentinelResource, Callable<?> callable) {
        Object result = null;
        if (StringUtils.isNotBlank(sentinelResource.getContextName())) {
            String actualOrigin = Optional.ofNullable(sentinelResource.getOrigin()).orElse(DEFAULT_ORIGIN);
            ContextUtil.enter(sentinelResource.getContextName(), actualOrigin);
        }
        Entry entry = null;
        try {
            entry = getEntry(sentinelResource);
            result = callable.call();
        } catch (Throwable e) {
            if (!BlockException.isBlockException(e)) {
                Tracer.trace(e);
            }
            log.error("[flowsphere] resource limit error resourceName={} contextName={} origin={}",
                    sentinelResource.getResourceName(), sentinelResource.getContextName(), sentinelResource.getOrigin(), e);
            throw e;
        } finally {
            if (Objects.nonNull(entry)) {
                entry.exit();
            }
            ContextUtil.exit();
        }
        return result;
    }

}
