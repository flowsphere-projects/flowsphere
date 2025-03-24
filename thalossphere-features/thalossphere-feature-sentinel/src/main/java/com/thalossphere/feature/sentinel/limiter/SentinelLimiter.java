package com.thalossphere.feature.sentinel.limiter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

public interface SentinelLimiter {

    Object limit(SentinelResource sentinelResource, Callable<?> callable);

    @SneakyThrows
    default Entry getEntry(SentinelResource sentinelResource) {
        return SphU.entry(sentinelResource.getResourceName());
    }

}
