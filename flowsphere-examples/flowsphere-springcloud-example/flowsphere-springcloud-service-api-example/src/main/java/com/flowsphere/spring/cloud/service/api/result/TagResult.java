package com.flowsphere.spring.cloud.service.api.result;


import com.flowsphere.common.tag.context.TagManager;
import lombok.Data;

@Data
public class TagResult {

    private String tag;

    private String systemTag;

    private String consumer;



    public static TagResult build(String consumer) {
        TagResult tagResult = new TagResult();
        tagResult.setTag(TagManager.getTag());
        tagResult.setSystemTag(TagManager.getSystemTag());
        tagResult.setConsumer(consumer);
        return tagResult;
    }


}
