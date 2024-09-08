package com.flowsphere.common.request;

import com.flowsphere.common.utils.StringUtils;

import java.util.List;

public abstract class AbstractAttributeResolver implements AttributeResolver {

    private final RequestResolver requestResolver;

    public AbstractAttributeResolver(RequestResolver requestResolver) {
        this.requestResolver = requestResolver;
    }

    public String getTag() {
        for (AttributeResolver attributeResolver : getAttributeResolverList()) {
            String tag = attributeResolver.getTag(requestResolver);
            if (StringUtils.isNotEmpty(tag)) {
                return tag;
            }
        }
        return null;
    }

    public String getUserId() {
        for (AttributeResolver attributeResolver : getAttributeResolverList()) {
            String userId = attributeResolver.getUserId(requestResolver);
            if (StringUtils.isNotEmpty(userId)) {
                return userId;
            }
        }
        return null;
    }

    public String getRegion() {
        for (AttributeResolver attributeResolver : getAttributeResolverList()) {
            String region = attributeResolver.getRegion(requestResolver);
            if (StringUtils.isNotEmpty(region)) {
                return region;
            }
        }
        return null;
    }

    public abstract List<AttributeResolver> getAttributeResolverList();

}
