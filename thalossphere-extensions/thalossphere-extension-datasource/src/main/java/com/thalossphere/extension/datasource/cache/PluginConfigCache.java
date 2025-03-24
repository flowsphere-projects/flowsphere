package com.thalossphere.extension.datasource.cache;

import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class PluginConfigCache {

    private static final String CACHE_KEY = "thalosspherePluginConfigCacheKey";

    private static final LoadingCache<String, PluginConfig> CAHCE;

    static {
        CAHCE = Caffeine.newBuilder()
                .expireAfterWrite(365 * 100, TimeUnit.DAYS)
                .initialCapacity(2)
                .maximumSize(10)
                .recordStats()
                .build(new CacheLoader<String, PluginConfig>() {
                    @Override
                    public PluginConfig load(String key) throws Exception {
                        return null;
                    }
                });
    }

    public static boolean put(PluginConfig ruleEntity) {
        CAHCE.put(CACHE_KEY, ruleEntity);
        return Boolean.TRUE;
    }

    public static PluginConfig get() {
        try {
            return CAHCE.get(CACHE_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean clear() {
        CAHCE.invalidate(CACHE_KEY);
        return Boolean.TRUE;
    }

}