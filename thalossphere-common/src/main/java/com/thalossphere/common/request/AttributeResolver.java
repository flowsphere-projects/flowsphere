package com.thalossphere.common.request;

public interface AttributeResolver {

    default String getTag(RequestResolver requestResolver) {
        return null;
    }

    default String getUserId(RequestResolver requestResolver) {
        return null;
    }

    default String getRegion(RequestResolver requestResolver) {
        return null;
    }

}
