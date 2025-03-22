package com.thalossphere.common.tag.extract;

import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.request.AttributeResolver;
import com.thalossphere.common.request.RequestResolver;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.common.utils.StringUtils;

public class ClientDefaultTagExtract implements TagExtract {


    @Override
    public String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        TagContext.set(attributeResolver.getTag());
        return attributeResolver.getTag();
    }

    @Override
    public boolean match(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        return StringUtils.isNotEmpty(attributeResolver.getTag());
    }

}
