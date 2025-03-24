package com.thalossphere.test.plugin.rocketmq;

import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RocketMQConfig;
import org.apache.rocketmq.client.common.ThreadLocalIndex;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;
import com.google.common.collect.Lists;

import com.thalossphere.agent.plugin.rocketmq.producer.MQFaultStrategyInterceptor;
import org.apache.rocketmq.client.impl.producer.TopicPublishInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MQFaultStrategyInterceptorTest {

    @Test
    public void beforeTest() {
        PluginConfig pluginConfig = new PluginConfig();
        RocketMQConfig rocketMQConfig = new RocketMQConfig();

        List<RocketMQConfig.ProducerConfig> producerConfigList = new ArrayList<>();
        RocketMQConfig.ProducerConfig producerConfig = new RocketMQConfig.ProducerConfig();
        producerConfig.setTopic("producer_topic");
        producerConfig.setQueueIdList(Lists.newArrayList(0, 1));
        producerConfig.setTag("myTag");


        producerConfigList.add(producerConfig);
        rocketMQConfig.setProducerConfigList(producerConfigList);
        pluginConfig.setRocketMQConfig(rocketMQConfig);

        PluginConfigCache.put(pluginConfig);

        TopicPublishInfo topicPublishInfo = new TopicPublishInfo();
        topicPublishInfo.setOrderTopic(false);
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic("producer_topic");
        messageQueue.setBrokerName("producer_broker");
        messageQueue.setQueueId(0);

        MessageQueue messageQueue1 = new MessageQueue();
        messageQueue1.setTopic("producer_topic");
        messageQueue1.setBrokerName("producer_broker");
        messageQueue1.setQueueId(2);

        MessageQueue messageQueue2 = new MessageQueue();
        messageQueue2.setTopic("producer_topic");
        messageQueue2.setBrokerName("producer_broker");
        messageQueue2.setQueueId(3);

        topicPublishInfo.setMessageQueueList(Lists.newArrayList(messageQueue, messageQueue1, messageQueue2));
        topicPublishInfo.setSendWhichQueue(new ThreadLocalIndex());
        topicPublishInfo.setHaveTopicRouterInfo(false);
        topicPublishInfo.setTopicRouteData(new TopicRouteData());

        Object[] objects = new Object[]{topicPublishInfo};

        InstantMethodInterceptorResult result = new InstantMethodInterceptorResult();
        MQFaultStrategyInterceptor interceptor = new MQFaultStrategyInterceptor();

        TagContext.set("myTag");
        interceptor.beforeMethod(null, objects, null, null, result);

        Assertions.assertTrue(topicPublishInfo.getMessageQueueList().size() == 1);
    }

}
