package com.flowsphere.common.request;

import com.flowsphere.common.constant.CommonConstant;

public interface ParameterResolver extends AttributeResolver {

    @Override
    default String getTag(RequestResolver requestResolver) {
        return requestResolver.getParameters(CommonConstant.TAG);
    }

    @Override
    default String getUserId(RequestResolver requestResolver) {
        return requestResolver.getParameters(CommonConstant.USER_ID);
    }

    @Override
    default String getRegion(RequestResolver requestResolver) {
        return requestResolver.getParameters(CommonConstant.REGION);
    }
}
