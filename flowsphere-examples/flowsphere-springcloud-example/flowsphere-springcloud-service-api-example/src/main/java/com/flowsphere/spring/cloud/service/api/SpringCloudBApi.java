package com.flowsphere.spring.cloud.service.api;

import com.flowsphere.spring.cloud.service.api.result.TagResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-b")
public interface SpringCloudBApi {

    @PostMapping("/service-b/helloWord")
    List<TagResult> helloWord(@RequestBody String str);

    @PostMapping("/service-b/helloWord1")
    List<TagResult> repeat(@RequestBody String str);

}
