package com.thalossphere.common.tag.extract;

import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.request.AttributeResolver;
import com.thalossphere.common.request.RequestResolver;

public interface TagExtract {

    String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

    boolean match(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

}
