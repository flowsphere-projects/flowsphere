package com.flowsphere.common.request;

import com.google.common.collect.Lists;

import java.util.List;

public class SimpleAttributeResolver extends AbstractAttributeResolver {

    public SimpleAttributeResolver(RequestResolver requestResolver) {
        super(requestResolver);
    }

    @Override
    public List<AttributeResolver> getAttributeResolverList() {
        return Lists.newArrayList(new CookieResolver() {}, new HeaderResolver() {}, new ParameterResolver() {});
    }

}
