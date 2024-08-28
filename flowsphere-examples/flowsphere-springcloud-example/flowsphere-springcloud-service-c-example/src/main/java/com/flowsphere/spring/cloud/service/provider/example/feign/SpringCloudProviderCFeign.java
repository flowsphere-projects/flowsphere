package com.flowsphere.spring.cloud.service.provider.example.feign;

import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.spring.cloud.service.api.SpringCloudCApi;
import com.flowsphere.spring.cloud.service.api.result.TagResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class SpringCloudProviderCFeign implements SpringCloudCApi {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @PostMapping("/service-c/helloWord")
    public TagResult helloWord(@RequestBody String str) {
        log.info("SpringCloudProviderCFeign helloWord str={}", str);
        Message msg = null;
        try {
            msg = new Message("TopicTest",
                    "tagA",
                    "OrderID188",
                    str.getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.putUserProperty("user", "20");
            SendResult sendResult = defaultMQProducer.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return TagResult.build("SpringCloudProviderCFeign");
    }

}
