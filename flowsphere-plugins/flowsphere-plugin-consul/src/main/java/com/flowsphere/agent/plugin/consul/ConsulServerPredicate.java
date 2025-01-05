package com.flowsphere.agent.plugin.consul;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.consul.discovery.ConsulServer;

import java.util.function.Predicate;

@Slf4j
public class ConsulServerPredicate implements Predicate<ConsulServer> {

    @Override
    public boolean test(ConsulServer consulServer) {
        String serverTag = consulServer.getMetadata().get(CommonConstant.SERVER_TAG);
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] consul predicate tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag) && !tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
            return false;
        }
        return true;
    }

}
