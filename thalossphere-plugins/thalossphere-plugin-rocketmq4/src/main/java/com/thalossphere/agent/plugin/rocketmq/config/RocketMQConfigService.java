package com.thalossphere.agent.plugin.rocketmq.config;

import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RocketMQConfig;

import java.util.List;
import java.util.Objects;

public class RocketMQConfigService {


    public static List<RocketMQConfig.ProducerConfig> getProducerConfigList() {
        RocketMQConfig rocketMQConfig = getRocketMQConfig();
        if (Objects.isNull(rocketMQConfig)) {
            return null;
        }
        List<RocketMQConfig.ProducerConfig> producerConfigList = rocketMQConfig.getProducerConfigList();
        return producerConfigList;
    }


    public static List<RocketMQConfig.ConsumerConfig> getConsumerConfigList(){
        RocketMQConfig rocketMQConfig = getRocketMQConfig();
        if (Objects.isNull(rocketMQConfig)) {
            return null;
        }
        List<RocketMQConfig.ConsumerConfig> consumerConfigList = rocketMQConfig.getConsumerConfigList();
        return consumerConfigList;
    }


    private static RocketMQConfig getRocketMQConfig() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig)) {
            return null;
        }
        return pluginConfig.getRocketMQConfig();
    }






}
