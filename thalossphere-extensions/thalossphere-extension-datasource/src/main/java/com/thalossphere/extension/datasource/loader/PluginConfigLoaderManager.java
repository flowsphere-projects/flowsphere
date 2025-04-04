package com.thalossphere.extension.datasource.loader;


import com.thalossphere.extension.datasource.enums.PluginConfigDataSourceTypeEnum;
import com.thalossphere.extension.datasource.support.apollo.ApolloPluginConfigLoader;
import com.thalossphere.extension.datasource.support.local.LocalPluginConfigLoader;
import com.thalossphere.extension.datasource.support.nacos.NacosPluginConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class PluginConfigLoaderManager {

    private static final Map<String, PluginConfigLoader> LOADER_MAP = new HashMap<>();

    static {
        LOADER_MAP.put(PluginConfigDataSourceTypeEnum.NACOS.getType(), new NacosPluginConfigLoader());
        LOADER_MAP.put(PluginConfigDataSourceTypeEnum.LOCAL.getType(), new LocalPluginConfigLoader());
        LOADER_MAP.put(PluginConfigDataSourceTypeEnum.APOLLO.getType(), new ApolloPluginConfigLoader());
    }


    public static PluginConfigLoader getPluginConfigLoader(String key) {
        return LOADER_MAP.get(key);
    }

}
