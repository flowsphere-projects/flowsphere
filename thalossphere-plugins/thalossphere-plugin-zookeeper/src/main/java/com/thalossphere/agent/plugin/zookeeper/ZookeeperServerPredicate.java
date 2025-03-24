package com.thalossphere.agent.plugin.zookeeper;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import java.util.function.Predicate;

@Slf4j
public class ZookeeperServerPredicate implements Predicate<ZookeeperServer> {

    @Override
    public boolean test(ZookeeperServer zookeeperServer) {
        String serverTag = zookeeperServer.getInstance().getPayload()
                .getMetadata().get(CommonConstant.SERVER_TAG);
        String tag = TagContext.get();
        if (log.isDebugEnabled()) {
            log.debug("[thalossphere] zookeeper predicate zk tag={}", tag);
        }
        if (!Strings.isNullOrEmpty(tag) && !tag.equals(serverTag) && !Strings.isNullOrEmpty(serverTag)) {
            return false;
        }
        return true;
    }

}
