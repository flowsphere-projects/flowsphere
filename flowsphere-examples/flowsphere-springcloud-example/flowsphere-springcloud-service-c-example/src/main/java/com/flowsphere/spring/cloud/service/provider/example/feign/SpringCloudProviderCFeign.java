package com.flowsphere.spring.cloud.service.provider.example.feign;

import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.spring.cloud.service.api.SpringCloudCApi;
import com.flowsphere.spring.cloud.service.api.result.TagResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class SpringCloudProviderCFeign implements SpringCloudCApi {

    @PostMapping("/service-c/helloWord")
    public TagResult helloWord(@RequestBody String str) {
        log.info("SpringCloudProviderCFeign helloWord str={}", str);
        return TagResult.build("SpringCloudProviderCFeign");
    }

}
