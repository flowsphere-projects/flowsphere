package com.thalossphere.rocketmq.example;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class MyTest {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupN123ame");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        Message msg = new Message("TopicTest",
                "tagA",
//                "OrderID188",
                "ttsar".getBytes(RemotingHelper.DEFAULT_CHARSET));
        msg.putUserProperty("user", "zhangsan1");
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult.getMessageQueue());
        System.out.println(sendResult.getSendStatus());
        producer.shutdown();
    }

}
