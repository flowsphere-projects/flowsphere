package com.thalossphere.agent.plugin.eureka;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import com.google.common.base.Strings;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class EurekaServerPredicate implements Predicate<DiscoveryEnabledServer> {

    @Override
    public boolean test(DiscoveryEnabledServer discoveryEnabledServer) {
        InstanceInfo instanceInfo = discoveryEnabledServer.getInstanceInfo();
        Map<String, String> metadata = instanceInfo.getMetadata();
        String serverTag = metadata.get(CommonConstant.SERVER_TAG);
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[thalossphere] eureka tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag) && tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
            return true;
        }
        return false;
    }

}
