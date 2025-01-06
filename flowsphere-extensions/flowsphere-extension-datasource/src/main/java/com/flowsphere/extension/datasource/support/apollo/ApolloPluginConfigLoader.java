package com.flowsphere.extension.datasource.support.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.loader.PluginConfigLoader;
import com.google.common.collect.Sets;

import java.util.Properties;

import static com.flowsphere.extension.datasource.support.apollo.ApolloConfigConstant.*;

public class ApolloPluginConfigLoader implements PluginConfigLoader {

    @Override
    public PluginConfig load(ClassLoader classLoader, Properties properties) {

        System.setProperty("env", (String) properties.get(ENV));
        System.setProperty("app.id", String.valueOf(properties.get(APP_ID)));
        System.setProperty("apollo.meta", (String) properties.get(APOLLO_META));

        Config config = ConfigService.getAppConfig();
        String json = config.getProperty(FLOW_SPHERE_CONFIG_JSON, null);
        config.addChangeListener(new ApolloConfigListener(), Sets.newHashSet(new String[]{FLOW_SPHERE_CONFIG_JSON}));
        return JacksonUtils.toObj(json, PluginConfig.class);

    }

}
