package com.flowsphere.agent.plugin.zookeeper;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import java.util.function.Predicate;

@Slf4j
public class ZookeeperServerPredicate implements Predicate<ZookeeperServer> {

    @Override
    public boolean test(ZookeeperServer zookeeperServer) {
        String serverTag = zookeeperServer.getInstance().getPayload()
                .getMetadata().get(CommonConstant.TAG_KEY);
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] ZookeeperServerPredicate zk tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag) && !tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
            return false;
        }
        return true;
    }

}
