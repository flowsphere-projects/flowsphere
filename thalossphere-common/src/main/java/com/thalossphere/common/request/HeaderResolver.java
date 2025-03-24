package com.thalossphere.common.request;

import com.thalossphere.common.constant.CommonConstant;

public interface HeaderResolver extends AttributeResolver {

    @Override
    default String getTag(RequestResolver requestResolver) {
        return requestResolver.getHeader(CommonConstant.TAG);
    }

    @Override
    default String getUserId(RequestResolver requestResolver) {
        return requestResolver.getHeader(CommonConstant.USER_ID);
    }

    @Override
    default String getRegion(RequestResolver requestResolver) {
        return requestResolver.getHeader(CommonConstant.REGION);
    }

}
