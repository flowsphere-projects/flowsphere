package com.flowsphere.extension.datasource.support.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.common.utils.StringUtils;
import com.flowsphere.extension.datasource.loader.PluginConfigLoader;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.Properties;

import static com.flowsphere.extension.datasource.support.nacos.NacosConfigConstant.*;


public class NacosPluginConfigLoader implements PluginConfigLoader {

    @SneakyThrows
    @Override
    public PluginConfig load(ClassLoader classLoader, Properties properties) {
        String dataId = properties.getProperty(DATA_ID);
        String groupId = properties.getProperty(GROUP_ID);
        int timeout = Integer.parseInt(Optional.ofNullable(properties.getProperty(TIMEOUT))
                .orElse(DEFAULT_TIMEOUT));
        ConfigService configService = NacosFactory.createConfigService(properties);
        configService.addListener(dataId, groupId, new NacosConfigListener());
        String jsonStr = configService.getConfig(dataId, groupId, timeout);
        if (StringUtils.isEmpty(jsonStr)) {
            return new PluginConfig();
        }
        return JacksonUtils.toObj(jsonStr, PluginConfig.class);
    }

}
