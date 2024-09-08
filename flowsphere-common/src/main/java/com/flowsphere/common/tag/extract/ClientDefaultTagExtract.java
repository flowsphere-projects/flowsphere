package com.flowsphere.common.tag.extract;

import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.request.AbstractAttributeResolver;
import com.flowsphere.common.request.AttributeResolver;
import com.flowsphere.common.request.RequestResolver;
import com.flowsphere.common.utils.StringUtils;

public class ClientDefaultTagExtract implements TagExtract {


    @Override
    public String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        return attributeResolver.getTag();
    }

    @Override
    public boolean match(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        return StringUtils.isNotEmpty(attributeResolver.getTag());
    }

}
