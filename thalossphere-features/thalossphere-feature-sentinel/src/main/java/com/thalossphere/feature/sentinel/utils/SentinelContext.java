package com.thalossphere.feature.sentinel.utils;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class SentinelContext {

    private static final ThreadLocal<BlockException> THREAD_LOCAL = new ThreadLocal<BlockException>();

    public static void set(BlockException exception) {
        THREAD_LOCAL.set(exception);
    }

    public static void remove() {
        if (THREAD_LOCAL.get() != null) {
            THREAD_LOCAL.remove();
        }
    }

    public static BlockException get() {
        return THREAD_LOCAL.get();
    }

}
