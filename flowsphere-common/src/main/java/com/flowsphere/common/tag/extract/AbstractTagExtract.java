package com.flowsphere.common.tag.extract;

import com.flowsphere.common.loadbalance.ArrayWeightRandom;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.loadbalance.TagWeight;
import com.flowsphere.common.request.AbstractAttributeResolver;
import com.flowsphere.common.tag.context.TagContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractTagExtract implements TagExtract {

    @Override
    public String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        List<TagWeight> tagWeight = getTagWeight(instantWeight);
        ArrayWeightRandom arrayWeightRandom = new ArrayWeightRandom(tagWeight);
        String tag = (String) arrayWeightRandom.choose();
        TagContext.set(tag);
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] spring-cloud-gateway choose tag={}", tag);
        }
        return tag;
    }

    public abstract List<TagWeight> getTagWeight(InstantWeight instantWeight);

}
