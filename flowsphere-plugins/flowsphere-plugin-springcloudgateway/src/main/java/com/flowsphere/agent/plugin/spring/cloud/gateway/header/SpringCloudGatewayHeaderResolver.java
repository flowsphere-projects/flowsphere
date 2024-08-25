package com.flowsphere.agent.plugin.spring.cloud.gateway.header;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.header.HeaderResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class SpringCloudGatewayHeaderResolver implements HeaderResolver {

    private final ServerHttpRequest request;

    public SpringCloudGatewayHeaderResolver(ServerHttpRequest request) {
        this.request = request;
    }

    @Override
    public String getTag() {
        List<String> tagList = request.getHeaders().get(CommonConstant.TAG);
        if (!CollectionUtils.isEmpty(tagList)) {
            return tagList.get(0);
        }
        return null;
    }

    @Override
    public String getUserId() {
        List<String> userIds = request.getHeaders().get(CommonConstant.USER_ID);
        if (!CollectionUtils.isEmpty(userIds)) {
            return userIds.get(0);
        }
        return null;
    }

    @Override
    public String getRegion() {
        List<String> regions = request.getHeaders().get(CommonConstant.REGION);
        if (!CollectionUtils.isEmpty(regions)) {
            return regions.get(0);
        }
        return null;
    }

}
