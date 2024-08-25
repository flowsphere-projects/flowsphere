package com.flowsphere.spring.cloud.service.api;

import com.flowsphere.spring.cloud.service.api.result.TagResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-c")
public interface SpringCloudCApi {

    @PostMapping(value = "/service-c/helloWord",produces = MediaType.APPLICATION_JSON_VALUE)
    TagResult helloWord(@RequestBody String str);

}
