package com.flowsphere.feature.removal;

import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RemovalInstanceService {

    private static final RemovalInstanceService INSTANCE = new RemovalInstanceService();

    public static RemovalInstanceService getInstance() {
        return INSTANCE;
    }

    public List<Server> removal(List<Server> instanceList) {
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        if (instanceCallResult.isEmpty()) {
            return instanceList;
        }
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return instanceList;
        }

        List<Server> removalInstanceList = getRemovalServiceNode(instanceList, instanceCallResult);

        List<Server> result = new ArrayList<>();
        for (Server server : instanceList) {
            ServiceNode instance = instanceCallResult.get(server.getHostPort());
            if (Objects.isNull(instance)) {
                continue;
            }
            if (isRemovalAllowed(instance, instanceList, removalInstanceList, removalConfig)) {
                instance.setRemovalTime(System.currentTimeMillis());
                instance.setRecoveryTime(System.currentTimeMillis() + removalConfig.getRecoveryTime());
                removalInstanceList.add(server);
            } else {
                //正常server
                result.add(server);
            }
        }
        return result;
    }

    private boolean isRemovalAllowed(ServiceNode instance, List<Server> instanceList,
                                     List<Server> removalInstanceList, RemovalConfig removalConfig) {
        return instance.getErrorRate() >= removalConfig.getErrorRate()
                && canMeetMinimumInstancesAfterRemoval(instanceList, removalInstanceList, removalConfig)
                && instance.getRemovalStatus().compareAndSet(false, true);
    }

    private List<Server> getRemovalServiceNode(List<Server> instanceList, Map<String, ServiceNode> instanceCallResult) {
        return instanceList.stream().filter(instance -> {
            ServiceNode serviceNode = instanceCallResult.get(instance.getHostPort());
            if (Objects.isNull(serviceNode)) {
                return true;
            }
            return serviceNode.getRemovalStatus().get();
        }).collect(Collectors.toList());
    }


    private boolean canMeetMinimumInstancesAfterRemoval(List<Server> instanceList, List<Server> removalInstanceList, RemovalConfig removalConfig) {
        return instanceList.size() - removalInstanceList.size() > removalConfig.getMinInstanceNum();
    }

}
