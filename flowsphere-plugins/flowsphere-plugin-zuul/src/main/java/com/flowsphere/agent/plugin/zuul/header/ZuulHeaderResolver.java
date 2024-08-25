package com.flowsphere.agent.plugin.zuul.header;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.header.HeaderResolver;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;

public class ZuulHeaderResolver implements HeaderResolver {

    private final RibbonCommandContext ribbonCommandContext;

    public ZuulHeaderResolver(RibbonCommandContext ribbonCommandContext) {
        this.ribbonCommandContext = ribbonCommandContext;
    }

    @Override
    public String getTag() {
        return ribbonCommandContext.getHeaders().getFirst(CommonConstant.TAG);
    }

    @Override
    public String getUserId() {
        return ribbonCommandContext.getHeaders().getFirst(CommonConstant.USER_ID);
    }

    @Override
    public String getRegion() {
        return ribbonCommandContext.getHeaders().getFirst(CommonConstant.REGION);
    }

}
