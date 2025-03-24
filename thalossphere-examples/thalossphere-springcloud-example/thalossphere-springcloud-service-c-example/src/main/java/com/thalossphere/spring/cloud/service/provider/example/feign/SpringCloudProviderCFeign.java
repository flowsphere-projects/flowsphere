package com.thalossphere.spring.cloud.service.provider.example.feign;

import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.common.tag.context.TagManager;
import com.thalossphere.common.utils.JacksonUtils;
import com.thalossphere.spring.cloud.service.api.SpringCloudCApi;
import com.thalossphere.spring.cloud.service.api.entity.TagEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class SpringCloudProviderCFeign implements SpringCloudCApi {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @SneakyThrows
    @PostMapping("/service-c/helloWord")
    public TagEntity helloWord(@RequestBody String str) {
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
        return TagEntity.build("SpringCloudProviderCFeign");
    }

    @PostMapping("/service-c/tagSpread")
    @Override
    public void tagSpread(List<TagEntity> tagList) {
        log.info("SpringCloudProviderC1Feign tagSpread tagList={} tag={}", tagList, TagContext.get());
        TagEntity tag = new TagEntity();
        tag.setTag(TagContext.get());
        tag.setSystemTag(TagManager.getSystemTag());
        tagList.add(tag);
        Message msg = null;
        try {
            msg = new Message("TopicTestAsync",
                    "*",
                    "OrderID188",
                    JacksonUtils.toJson(tagList).getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.putUserProperty("user", "20");
            SendResult sendResult = defaultMQProducer.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
