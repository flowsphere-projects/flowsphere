package com.flowsphere.rocketmq.example.controller;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class RocketMQController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @GetMapping("/myRocketMQ")
    public String myRocketMQ(String str) throws Exception {
        Message msg = new Message("TopicTest",
                "tagA",
//                "OrderID188",
                str.getBytes(RemotingHelper.DEFAULT_CHARSET));
        msg.putUserProperty("user", "zhangsan");
        SendResult sendResult = defaultMQProducer.send(msg);
        return "ok";
    }

}
