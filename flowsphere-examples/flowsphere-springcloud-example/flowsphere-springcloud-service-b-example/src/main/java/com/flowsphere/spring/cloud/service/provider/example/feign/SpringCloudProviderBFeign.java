package com.flowsphere.spring.cloud.service.provider.example.feign;

import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.spring.cloud.service.api.SpringCloudBApi;
import com.flowsphere.spring.cloud.service.api.SpringCloudCApi;
import com.flowsphere.spring.cloud.service.api.result.TagResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/service-b")
public class SpringCloudProviderBFeign implements SpringCloudBApi {

    @Autowired
    private SpringCloudCApi springCloudCApi;

    @PostMapping("/helloWord")
    public List<TagResult> helloWord(@RequestBody String str) {
        log.info("SpringCloudProviderB1Feign helloWord str={} tag={}", str, TagContext.get());
        List<TagResult> result = new ArrayList<>();
        result.add(springCloudCApi.helloWord(str));
        result.add(TagResult.build("SpringCloudProviderBFeign"));
        return result;
    }

    @PostMapping("/repeat")
    @Override
    public List<TagResult> repeat(String str) {
        log.info("SpringCloudProviderB1Feign helloWord1 str={} tag={}", str, TagContext.get());
        List<TagResult> result = new ArrayList<>();
        result.add(springCloudCApi.helloWord(str));
        result.add(TagResult.build("SpringCloudProviderBFeign"));
        return result;
    }

}
