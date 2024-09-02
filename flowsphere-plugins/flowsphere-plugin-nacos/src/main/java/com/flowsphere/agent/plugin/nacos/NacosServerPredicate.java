package com.flowsphere.agent.plugin.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Slf4j
public class NacosServerPredicate implements Predicate<NacosServer> {

    @Override
    public boolean test(NacosServer nacosServer) {
        String serverTag = nacosServer.getInstance().getMetadata().get(CommonConstant.SERVER_TAG);
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] NacosServerPredicate nacos tag={} serverTag={}", tag, serverTag);
        }
        if (!Strings.isNullOrEmpty(tag) && !tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
            return false;
        }
        return true;
    }

}
