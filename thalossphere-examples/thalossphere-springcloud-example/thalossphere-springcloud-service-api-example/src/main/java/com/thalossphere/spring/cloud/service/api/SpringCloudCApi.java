package com.thalossphere.spring.cloud.service.api;

import com.thalossphere.spring.cloud.service.api.entity.TagEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-c")
public interface SpringCloudCApi {

    @PostMapping(value = "/service-c/helloWord",produces = MediaType.APPLICATION_JSON_VALUE)
    TagEntity helloWord(@RequestBody String str);

    @PostMapping("/service-c/tagSpread")
    void tagSpread(@RequestBody List<TagEntity> tagList);

}
