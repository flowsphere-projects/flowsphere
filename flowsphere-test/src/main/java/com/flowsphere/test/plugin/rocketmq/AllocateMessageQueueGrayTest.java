package com.flowsphere.test.plugin.rocketmq;

import com.flowsphere.agent.plugin.rocketmq.consumer.AllocateMessageQueueGray;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RocketMQConfig;
import com.google.common.collect.Lists;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllocateMessageQueueGrayTest {


    @Test
    public void grayAllocateTest() {
        initRocketMQConfigCache();
        List<MessageQueue> messageQueueList = buildMessageQueueList();
        String cid = "127.0.0.1@grayTag@" + UUID.randomUUID().toString().replace("-", "");
        String cid1 = "192.168.0.1@grayTag@" + UUID.randomUUID().toString().replace("-", "");
        List<String> cidList = Lists.newArrayList(cid, cid1);

        AllocateMessageQueueGray allocateMessageQueueGray = new AllocateMessageQueueGray();

        System.setProperty("flowsphere.tag", "grayTag");
        List<MessageQueue> result = allocateMessageQueueGray.allocate("myConsumerGroupName", cid, messageQueueList, cidList);
        Assertions.assertTrue(result.size() == 1);
    }


    @Test
    public void notGrayNodeAllocateTest() {
        initRocketMQConfigCache();
        List<MessageQueue> messageQueueList = buildMessageQueueList();
        String cid = "127.0.0.1@grayTag@" + UUID.randomUUID().toString().replace("-", "");
        String cid1 = "192.168.0.1@grayTag@" + UUID.randomUUID().toString().replace("-", "");
        String cid2 = "192.168.0.2@" + UUID.randomUUID().toString().replace("-", "");
        List<String> cidList = Lists.newArrayList(cid, cid1, cid2);

        AllocateMessageQueueGray allocateMessageQueueGray = new AllocateMessageQueueGray();

        System.setProperty("flowsphere.tag", "grayTag");
        List<MessageQueue> result = allocateMessageQueueGray.allocate("myConsumerGroupName", cid2, messageQueueList, cidList);
        Assertions.assertTrue(result.size() == 2);
    }


    private void initRocketMQConfigCache() {

        PluginConfig pluginConfig = new PluginConfig();
        RocketMQConfig rocketMQConfig = new RocketMQConfig();

        List<RocketMQConfig.ConsumerConfig> consumerConfigList = new ArrayList<>();
        RocketMQConfig.ConsumerConfig consumerConfig = new RocketMQConfig.ConsumerConfig();
        consumerConfig.setConsumerGroupName("myConsumerGroupName");
        consumerConfig.setTag("grayTag");
        consumerConfig.setQueueIdList(Lists.newArrayList(0, 1));
        consumerConfigList.add(consumerConfig);

        rocketMQConfig.setConsumerConfigList(consumerConfigList);
        pluginConfig.setRocketMQConfig(rocketMQConfig);
        PluginConfigCache.put(pluginConfig);
    }


    private List<MessageQueue> buildMessageQueueList() {
        List<MessageQueue> messageQueueList = new ArrayList<>();
        MessageQueue messageQueue = new MessageQueue();
        messageQueue.setTopic("myTopic");
        messageQueue.setBrokerName("myBrokerName");
        messageQueue.setQueueId(0);
        messageQueueList.add(messageQueue);

        MessageQueue messageQueue1 = new MessageQueue();
        messageQueue1.setTopic("myTopic");
        messageQueue1.setBrokerName("myBrokerName");
        messageQueue1.setQueueId(1);
        messageQueueList.add(messageQueue1);

        MessageQueue messageQueue2 = new MessageQueue();
        messageQueue2.setTopic("myTopic");
        messageQueue2.setBrokerName("myBrokerName");
        messageQueue2.setQueueId(2);
        messageQueueList.add(messageQueue2);

        MessageQueue messageQueue3 = new MessageQueue();
        messageQueue3.setTopic("myTopic");
        messageQueue3.setBrokerName("myBrokerName");
        messageQueue3.setQueueId(3);
        messageQueueList.add(messageQueue3);
        return messageQueueList;
    }


}
