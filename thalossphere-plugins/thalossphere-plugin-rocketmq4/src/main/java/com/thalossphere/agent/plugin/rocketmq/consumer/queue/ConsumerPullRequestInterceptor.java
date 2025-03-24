package com.thalossphere.agent.plugin.rocketmq.consumer.queue;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RocketMQConfig;
import org.apache.rocketmq.client.impl.consumer.PullRequest;
import org.apache.rocketmq.common.message.MessageQueue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ConsumerPullRequestInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RocketMQConfig rocketMQConfig = pluginConfig.getRocketMQConfig();
//        if (!ModelType.QUEUE.getModelType().equals(rocketMQConfig.getModelType())) {
//            return;
//        }
        if (allArguments[0] instanceof PullRequest) {
            PullRequest pullRequest = (PullRequest) allArguments[0];

            MessageQueue messageQueue = pullRequest.getMessageQueue();

            List<RocketMQConfig.ConsumerConfig> consumerConfigList = rocketMQConfig.getConsumerConfigList();

            boolean isGray = consumerConfigList.stream().filter(
                            consumerGroupConfig ->
                                    consumerGroupConfig.getConsumerGroupName().equals(pullRequest.getConsumerGroup()))
                    .findFirst()
                    .map(RocketMQConfig.ConsumerConfig::getQueueIdList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .anyMatch(queueId -> queueId == messageQueue.getQueueId());
            instantMethodInterceptorResult.setContinue(isGray);

        }
    }

}
