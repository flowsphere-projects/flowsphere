package com.flowsphere.common.request;

import com.flowsphere.common.constant.CommonConstant;

public interface CookieResolver extends AttributeResolver {


    @Override
    default String getTag(RequestResolver requestResolver) {
        return requestResolver.getCookie(CommonConstant.TAG);
    }

    @Override
    default String getUserId(RequestResolver requestResolver) {
        return requestResolver.getCookie(CommonConstant.USER_ID);
    }

    @Override
    default String getRegion(RequestResolver requestResolver) {
        return requestResolver.getCookie(CommonConstant.REGION);
    }

}
