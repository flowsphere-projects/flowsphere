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
    public List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> mqAll, List<String> cidAll) {
        RocketMQConfig.ConsumerConfig grayConsumerConfig = getGrayConsumerConfig(consumerGroup);
        //当前不是灰度消费者直接计算消费队列
        //consumerGroup是灰度消费者且当前Cid不是灰度Cid，cidAll则需要剔除灰度cid，同时mqAll需要剔除灰度队列
        if (Objects.isNull(grayConsumerConfig)) {
            return DEFAULT.allocate(consumerGroup, currentCID, mqAll, cidAll);
        }
        boolean isGrayCid = isGrayCid(currentCID);
        if (!isGrayCid) {
            if (log.isDebugEnabled()) {
                log.debug("[flowsphere] gray not-consumer grayConsumerConfig={}", grayConsumerConfig);
            }
            //cidAll则需要剔除灰度cid，同时mqAll需要剔除灰度队列
            List<String> newCidList = cidAll.stream().filter(cid -> !cid.contains(grayConsumerConfig.getTag())).collect(Collectors.toList());
            List<MessageQueue> normalMessageQueueList = filterMessageQueue(mqAll, messageQueue -> !grayConsumerConfig.getQueueIdList().stream()
                    .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
            return DEFAULT.allocate(consumerGroup, currentCID, normalMessageQueueList, newCidList);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("[flowsphere] gray consumer grayConsumerConfig={}", grayConsumerConfig);
            }
            //当前Cid是灰度节点
            List<MessageQueue> grayMessageQueueList = filterMessageQueue(mqAll, messageQueue -> grayConsumerConfig.getQueueIdList().stream()
                    .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
            List<String> includeSystemTagCidList = filterCid(cidAll, new CidSplitMatchGrayCondition());
            if (CollectionUtils.isNotEmpty(grayMessageQueueList) && CollectionUtils.isNotEmpty(includeSystemTagCidList)) {
                return DEFAULT.allocate(consumerGroup, currentCID, grayMessageQueueList, includeSystemTagCidList);
            }
        }


        return DEFAULT.allocate(consumerGroup, currentCID, mqAll, cidAll);
    }

    private boolean isGrayCid(String currentCID) {
        return currentCID.contains(TagManager.getSystemTag());
    }


    private RocketMQConfig.ConsumerConfig getGrayConsumerConfig(String consumerGroup) {
        return RocketMQConfigService.getConsumerConfigList()
                .stream()
                .filter(consumerConfig -> {
                    if (consumerGroup.equals(consumerConfig.getConsumerGroupName()) && consumerConfig.getTag().equals(TagManager.getTag())) {
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
