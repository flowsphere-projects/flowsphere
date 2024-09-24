package com.flowsphere.spring.cloud.service.api;

import com.flowsphere.spring.cloud.service.api.entity.TagEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-b")
public interface SpringCloudBApi {

    @PostMapping("/service-b/helloWord")
    List<TagEntity> helloWord(@RequestBody String str);

    @PostMapping("/service-b/repeat")
    List<TagEntity> repeat(@RequestBody String str);

    @PostMapping("/service-b/tagSpread")
    void tagSpread(@RequestBody List<TagEntity> tagList);

}
