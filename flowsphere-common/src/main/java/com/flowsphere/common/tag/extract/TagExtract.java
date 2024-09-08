package com.flowsphere.common.tag.extract;

import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.request.AbstractAttributeResolver;
import com.flowsphere.common.request.AttributeResolver;
import com.flowsphere.common.request.RequestResolver;

public interface TagExtract {

    String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

    boolean match(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

}
