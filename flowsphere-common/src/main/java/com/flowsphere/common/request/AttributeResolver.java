package com.flowsphere.common.request;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.utils.StringUtils;

public interface AttributeResolver {

    default String getTag(RequestResolver requestResolver) {
        String tag = requestResolver.getHeader(CommonConstant.TAG);
        if (StringUtils.isNotEmpty(tag)) {
            return tag;
        }
        tag = requestResolver.getParameters(CommonConstant.TAG);
        if (StringUtils.isNotEmpty(tag)) {
            return tag;
        }
        tag = requestResolver.getCookie(CommonConstant.TAG);
        if (StringUtils.isNotEmpty(tag)) {
            return tag;
        }
        return null;
    }

    default String getUserId(RequestResolver requestResolver) {
        String userId = requestResolver.getHeader(CommonConstant.USER_ID);
        if (StringUtils.isNotEmpty(userId)) {
            return userId;
        }
        userId = requestResolver.getParameters(CommonConstant.USER_ID);
        if (StringUtils.isNotEmpty(userId)) {
            return userId;
        }
        userId = requestResolver.getCookie(CommonConstant.USER_ID);
        if (StringUtils.isNotEmpty(userId)) {
            return userId;
        }
        return null;
    }

    default String getRegion(RequestResolver requestResolver) {
        String region = requestResolver.getHeader(CommonConstant.REGION);
        if (StringUtils.isNotEmpty(region)) {
            return region;
        }
        region = requestResolver.getParameters(CommonConstant.REGION);
        if (StringUtils.isNotEmpty(region)) {
            return region;
        }
        region = requestResolver.getCookie(CommonConstant.REGION);
        if (StringUtils.isNotEmpty(region)) {
            return region;
        }
        return null;
    }


}
