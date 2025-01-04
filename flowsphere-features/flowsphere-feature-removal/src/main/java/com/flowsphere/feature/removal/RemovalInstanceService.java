package com.flowsphere.feature.removal;

import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RemovalInstanceService {

    private static final RemovalInstanceService INSTANCE = new RemovalInstanceService();

    public static RemovalInstanceService getInstance() {
        return INSTANCE;
    }

    public List<Server>  removal(List<Server> instanceList) {
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        if (instanceCallResult.isEmpty()) {
            return instanceList;
        }
        RemovalConfig removalConfig = getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return instanceList;
        }
        List<Server> removalInstanceList = getRemovalServiceNode(instanceList, instanceCallResult);
        double canRemovalNum = getCanRemovalNum(instanceList, removalInstanceList, removalConfig);
        if (canRemovalNum <= 0) {
            return instanceList;
        }
        return removal(instanceList, instanceCallResult, removalConfig, canRemovalNum, removalInstanceList);
    }


    private List<Server> removal(List<Server> instanceList, Map<String, ServiceNode> instanceCallResult, RemovalConfig removalConfig,
                                 double canRemovalNum, List<Server> removalInstanceList) {
        List<Server> result = new ArrayList<>();
        for (Server server : instanceList) {
            ServiceNode instance = instanceCallResult.get(server.getHostPort());
            if (Objects.isNull(instance)) {
                continue;
            }
            if (isRemovalAllowed(instance, removalConfig, canRemovalNum)) {
                instance.setRemovalTime(System.currentTimeMillis());
                instance.setRecoveryTime(System.currentTimeMillis() + removalConfig.getRecoveryTime());
                removalInstanceList.add(server);
                canRemovalNum--;
            } else {
                result.add(server);
            }
        }
        log.info("[flowsphere] RemovalInstanceService removal normalInstance={} removalInstance={}", result, removalInstanceList);
        return result;
    }

    private RemovalConfig getRemovalConfig() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        return pluginConfig.getRemovalConfig();
    }

    public double getCanRemovalNum(List<Server> instanceList, List<Server> removalInstanceList, RemovalConfig removalConfig) {
        int removalCount = removalInstanceList.size() > 0 ? instanceList.size() - removalInstanceList.size() : 0;
        double canRemovalNum = Math.min(instanceList.size() * removalConfig.getScaleUpLimit() - removalCount,
                instanceList.size() - removalCount - removalConfig.getMinInstanceNum());
        return canRemovalNum;
    }

    public boolean isRemovalAllowed(ServiceNode instance, RemovalConfig removalConfig, double canRemovalNum) {
        return instance.getErrorRate() >= removalConfig.getErrorRate()
                && canRemovalNum >= 1
                && instance.getRemovalStatus().compareAndSet(false, true);
    }

    public List<Server> getRemovalServiceNode(List<Server> instanceList, Map<String, ServiceNode> instanceCallResult) {
        return instanceList.stream().filter(instance -> {
            ServiceNode serviceNode = instanceCallResult.get(instance.getHostPort());
            if (Objects.isNull(serviceNode)) {
                return true;
            }
            return serviceNode.getRemovalStatus().get();
        }).collect(Collectors.toList());
    }

}
