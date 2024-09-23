package com.flowsphere.spring.cloud.service.api.entity;


import com.flowsphere.common.tag.context.TagManager;
import lombok.Data;

@Data
public class TagEntity {

    private String tag;

    private String systemTag;

    private String consumer;



    public static TagEntity build(String consumer) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setTag(TagManager.getTag());
        tagEntity.setSystemTag(TagManager.getSystemTag());
        tagEntity.setConsumer(consumer);
        return tagEntity;
    }


}
