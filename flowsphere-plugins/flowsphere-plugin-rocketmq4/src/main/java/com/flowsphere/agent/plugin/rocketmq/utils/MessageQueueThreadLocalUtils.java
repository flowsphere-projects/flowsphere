package com.flowsphere.agent.plugin.rocketmq.utils;

import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

public class MessageQueueThreadLocalUtils {

    private static final ThreadLocal<List<MessageQueue>> QUEUE_LIST = new ThreadLocal<List<MessageQueue>>();


    public static void set(List<MessageQueue> queueList) {
        QUEUE_LIST.set(queueList);
    }

    public static void remove() {
        if (QUEUE_LIST.get() != null) {
            QUEUE_LIST.remove();
        }
    }

    public static List<MessageQueue> get() {
        return QUEUE_LIST.get();
    }

}
