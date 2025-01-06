package com.flowsphere.spring.cloud.service.provider.example.feign;

import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.spring.cloud.service.api.SpringCloudBApi;
import com.flowsphere.spring.cloud.service.api.SpringCloudCApi;
import com.flowsphere.spring.cloud.service.api.entity.TagEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/service-b")
public class SpringCloudProviderBFeign implements SpringCloudBApi {

    @Autowired
    private SpringCloudCApi springCloudCApi;

    @PostMapping("/helloWord")
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<TagEntity> helloWord(@RequestBody String str) {
        log.info("SpringCloudProviderBFeign helloWord str={} tag={}", str, TagContext.get());
        List<TagEntity> result = new ArrayList<>();
        result.add(springCloudCApi.helloWord(str));
        result.add(TagEntity.build("SpringCloudProviderBFeign"));
        return result;
    }

    @PostMapping("/repeat")
    @Override
    public List<TagEntity> repeat(String str) {
        log.info("SpringCloudProviderBFeign helloWord1 str={} tag={}", str, TagContext.get());
        List<TagEntity> result = new ArrayList<>();
        result.add(springCloudCApi.helloWord(str));
        result.add(TagEntity.build("SpringCloudProviderBFeign"));
        return result;
    }

    @PostMapping("/tagSpread")
    @Override
    public void tagSpread(List<TagEntity> tagList) {
        log.info("SpringCloudProviderBFeign tagSpread tagList={} tag={}", tagList, TagContext.get());
        TagEntity tag = new TagEntity();
        tag.setTag(TagContext.get());
        tag.setSystemTag(TagManager.getSystemTag());
        tagList.add(tag);
        springCloudCApi.tagSpread(tagList);
    }

}
