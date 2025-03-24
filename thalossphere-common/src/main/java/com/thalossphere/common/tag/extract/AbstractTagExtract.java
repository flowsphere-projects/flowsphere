package com.thalossphere.common.tag.extract;

import com.thalossphere.common.loadbalance.ArrayWeightRandom;
import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.loadbalance.TagWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.tag.context.TagContext;
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
            log.debug("[thalossphere] spring-cloud-gateway choose tag={}", tag);
        }
        return tag;
    }

    public abstract List<TagWeight> getTagWeight(InstantWeight instantWeight);

}
