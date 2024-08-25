package com.flowsphere.common.tag.extract;

import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.utils.StringUtils;

public class ClientDefaultTagExtract implements TagExtract {


    @Override
    public String extract(InstantWeight instantWeight, HeaderResolver headerResolver) {
        return headerResolver.getTag();
    }

    @Override
    public boolean match(InstantWeight instantWeight, HeaderResolver headerResolver) {
        return StringUtils.isNotEmpty(headerResolver.getTag());
    }

}
