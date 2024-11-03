package com.flowsphere.extension.datasource.entity;

import lombok.Data;

@Data
public class PluginConfig {

    private RocketMQConfig rocketMQConfig;

    private ElasticJobConfig elasticJobConfig;

    private SpringCloudGatewayConfig springCloudGatewayConfig;

    private ZuulConfig zuulConfig;

    private SentinelConfig sentinelConfig;


}
