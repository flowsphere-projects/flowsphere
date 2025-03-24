package com.thalossphere.common.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class YamlAgentConfigCache {

    private static final String CACHE_KEY = "thalossphereYamlAgentConfigCacheKey";

    private static final LoadingCache<String, YamlAgentConfig> CAHCE;

    static {
        CAHCE = Caffeine.newBuilder()
                .expireAfterWrite(365 * 100, TimeUnit.DAYS)
                .initialCapacity(2)
                .maximumSize(10)
                .recordStats()
                .build(new CacheLoader<String, YamlAgentConfig>() {
                    @Override
                    public YamlAgentConfig load(String key) throws Exception {
                        return null;
                    }
                });
    }

    public static boolean put(YamlAgentConfig ruleEntity) {
        CAHCE.put(CACHE_KEY, ruleEntity);
        return Boolean.TRUE;
    }

    public static YamlAgentConfig get() {
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