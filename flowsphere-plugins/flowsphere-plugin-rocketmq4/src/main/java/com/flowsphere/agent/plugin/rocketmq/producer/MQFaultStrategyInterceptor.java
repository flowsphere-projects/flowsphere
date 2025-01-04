package com.flowsphere.agent.plugin.rocketmq.producer;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.agent.plugin.rocketmq.config.RocketMQConfigService;
import com.flowsphere.agent.plugin.rocketmq.utils.MessageQueueThreadLocalUtils;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.extension.datasource.entity.RocketMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.impl.producer.TopicPublishInfo;
import org.apache.rocketmq.common.message.MessageQueue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class MQFaultStrategyInterceptor implements InstantMethodInterceptor {


    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        TopicPublishInfo topicPublishInfo = (TopicPublishInfo) allArguments[0];
        MessageQueueThreadLocalUtils.set(topicPublishInfo.getMessageQueueList());
        List<RocketMQConfig.ProducerConfig> producerConfigList = RocketMQConfigService.getProducerConfigList();
        if (CollectionUtils.isEmpty(producerConfigList)) {
            return;
        }
        MessageQueue firstMessageQueue = topicPublishInfo.getMessageQueueList().get(0);
        redefineMessageQueue(topicPublishInfo, firstMessageQueue, producerConfigList);
    }

    private void redefineMessageQueue(TopicPublishInfo topicPublishInfo, Predicate<MessageQueue> condition) {
        List<MessageQueue> result = topicPublishInfo.getMessageQueueList()
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
        topicPublishInfo.setMessageQueueList(result);
    }


    private void redefineMessageQueue(TopicPublishInfo topicPublishInfo, MessageQueue firstMessageQueue, List<RocketMQConfig.ProducerConfig> producerConfigList) {
        for (RocketMQConfig.ProducerConfig producerConfig : producerConfigList) {
            if (producerConfig.getTopic().equals(firstMessageQueue.getTopic())) {
                if (TagManager.getTag().equals(producerConfig.getTag())) {
                    //只发送topic灰度队列
                    if (log.isDebugEnabled()) {
                        log.debug("[flowsphere] MQFaultStrategyInterceptor only send gray queues producerConfig={}", producerConfig);
                    }
                    redefineMessageQueue(topicPublishInfo, messageQueue -> producerConfig.getQueueIdList().stream()
                            .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
                } else {
                    //剔除topic灰度队列，避免非灰度环境消息
                    if (log.isDebugEnabled()) {
                        log.debug("[flowsphere] MQFaultStrategyInterceptor send non-gray queue producerConfig={}", producerConfig);
                    }
                    redefineMessageQueue(topicPublishInfo, messageQueue -> !producerConfig.getQueueIdList().stream()
                            .anyMatch(queueId -> queueId.equals(messageQueue.getQueueId())));
                }
                return;
            }
        }
    }


    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        try {
            TopicPublishInfo topicPublishInfo = (TopicPublishInfo) allArguments[0];
            topicPublishInfo.setMessageQueueList(MessageQueueThreadLocalUtils.get());
        } finally {
            MessageQueueThreadLocalUtils.remove();
        }
    }
}
