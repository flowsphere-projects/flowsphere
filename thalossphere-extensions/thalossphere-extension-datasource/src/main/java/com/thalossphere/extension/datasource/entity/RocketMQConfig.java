package com.thalossphere.extension.datasource.entity;

import lombok.Data;

import java.util.List;

@Data
public class RocketMQConfig {

    private List<ConsumerConfig> consumerConfigList;

    private List<ProducerConfig> producerConfigList;


    @Data
    public static class ConsumerConfig {

        private String consumerGroupName;

        private String tag;

        private List<Integer> queueIdList;

    }

    @Data
    public static class ProducerConfig {

        private String topic;

        private String tag;

        private List<Integer> queueIdList;

    }

}
