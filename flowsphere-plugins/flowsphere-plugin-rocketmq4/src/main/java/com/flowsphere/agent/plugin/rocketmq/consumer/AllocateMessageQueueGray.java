package com.flowsphere.agent.plugin.rocketmq.consumer;

import com.flowsphere.agent.plugin.rocketmq.config.RocketMQConfigService;
import com.flowsphere.agent.plugin.rocketmq.consumer.condition.CidSplitMatchGrayCondition;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.extension.datasource.entity.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AllocateMessageQueueGray implements AllocateMessageQueueStrategy {

    private static final AllocateMessageQueueAveragely DEFAULT = new AllocateMessageQueueAveragely();

    @Override
    public List<MessageQueue> allocate(String consumerGroup, String currentCid, List<MessageQueue> mqAll, List<String> cidAll) {

        MessageQueue fistQueue = mqAll.get(0);
        RocketMQConfig.ConsumerConfig grayConsumerConfig = getGrayConsumerConfig(consumerGroup);
        if (Objects.isNull(grayConsumerConfig) || fistQueue.getTopic().contains("%RETRY%")) {
            return DEFAULT.allocate(consumerGroup, currentCid, mqAll, cidAll);
        }
        boolean isGrayCid = isGrayCid(currentCid, grayConsumerConfig.getTag());
        if (!isGrayCid) {
            if (log.isDebugEnabled()) {
                log.debug("[flowsphere] gray not-consumer grayConsumerConfig={}", grayConsumerConfig);
            }
            //cidAll则需要剔除灰度cid，同时mqAll需要剔除灰度队列

            List<String> newCidList = cidAll.stream().filter(cid -> isGrayCid(cid, TagManager.getSystemTag())).collect(Collectors.toList());
            List<MessageQueue> normalMessageQueueList = filterMessageQueue(mqAll, messageQueue -> !grayConsumerConfig.getQueueIdList().stream()
                    .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
            return DEFAULT.allocate(consumerGroup, currentCid, normalMessageQueueList, newCidList);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("[flowsphere] gray consumer grayConsumerConfig={}", grayConsumerConfig);
            }
            //当前Cid是灰度节点
            List<MessageQueue> grayMessageQueueList = filterMessageQueue(mqAll, messageQueue -> grayConsumerConfig.getQueueIdList().stream()
                    .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
            List<String> includeSystemTagCidList = filterCid(cidAll, new CidSplitMatchGrayCondition(TagManager.getSystemTag()));
            if (CollectionUtils.isNotEmpty(grayMessageQueueList) && CollectionUtils.isNotEmpty(includeSystemTagCidList)) {
                return DEFAULT.allocate(consumerGroup, currentCid, grayMessageQueueList, includeSystemTagCidList);;
            }
        }

        return DEFAULT.allocate(consumerGroup, currentCid, mqAll, cidAll);
    }

    private boolean isGrayCid(String currentCid, String tag) {
        return new CidSplitMatchGrayCondition(tag).test(currentCid);
    }


    private RocketMQConfig.ConsumerConfig getGrayConsumerConfig(String consumerGroup) {
        return RocketMQConfigService.getConsumerConfigList()
                .stream()
                .filter(consumerConfig -> {
                    if (consumerGroup.equals(consumerConfig.getConsumerGroupName())) {
                        return true;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    private List<MessageQueue> filterMessageQueue(List<MessageQueue> mqAll, Predicate<MessageQueue> predicate) {
        return mqAll.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private List<String> filterCid(List<String> cidAll, Predicate<String> predicate) {
        return cidAll.stream().filter(predicate).collect(Collectors.toList());
    }


    @Override
    public String getName() {
        return "AllocateMessageQueueGray";
    }


}
