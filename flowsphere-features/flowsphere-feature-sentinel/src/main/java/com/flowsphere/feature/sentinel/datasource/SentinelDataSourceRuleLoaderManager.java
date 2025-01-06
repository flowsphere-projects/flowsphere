package com.flowsphere.feature.sentinel.datasource;

import java.util.HashMap;
import java.util.Map;

public class SentinelDataSourceRuleLoaderManager {

    private static final Map<String, SentinelDataSourceRuleLoader> SENTINEL_RULE_LOADER_MAP = new HashMap<>();

    static {
        SENTINEL_RULE_LOADER_MAP.put(SentinelConfigDataSourceTypeEnum.NACOS.getType(), new SentinelNacosDataSourceRuleLoader());
        SENTINEL_RULE_LOADER_MAP.put(SentinelConfigDataSourceTypeEnum.APOLLO.getType(), new SentinelApolloDataSourceRuleLoader());
;    }

    public static SentinelDataSourceRuleLoader getLoader(String key) {
        return SENTINEL_RULE_LOADER_MAP.get(key);
    }

}
