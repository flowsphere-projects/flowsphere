package com.flowsphere.extension.datasource.support.apollo;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;

import static com.flowsphere.extension.datasource.support.apollo.ApolloConfigConstant.FLOW_SPHERE_CONFIG_JSON;

public class ApolloConfigListener implements ConfigChangeListener {

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        ConfigChange configChange = changeEvent.getChange(FLOW_SPHERE_CONFIG_JSON);
        PluginConfig pluginConfig = JacksonUtils.toObj(configChange.getNewValue(), PluginConfig.class);
        PluginConfigCache.put(pluginConfig);
    }

}
