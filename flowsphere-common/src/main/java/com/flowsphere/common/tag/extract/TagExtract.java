package com.flowsphere.common.tag.extract;

import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;

public interface TagExtract {

    String extract(InstantWeight instantWeight, HeaderResolver headerResolver);

    boolean match(InstantWeight instantWeight, HeaderResolver headerResolver);

}
