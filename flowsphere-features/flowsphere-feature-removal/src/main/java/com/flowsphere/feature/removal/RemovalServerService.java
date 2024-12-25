package com.flowsphere.feature.removal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.netflix.loadbalancer.Server;

public class RemovalServerService {

    private static final RemovalServerService INSTANCE = new RemovalServerService();

    public static RemovalServerService getINSTANCE() {
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

        double canRemovalNum = getCanRemovalNum(instanceList, removalConfig);

        List<Server> result = new ArrayList<>();
        for (Server server : instanceList) {
            ServiceNode instance = instanceCallResult.get(server.getHostPort());
            if (Objects.isNull(instance)) {
                continue;
            }
            if (instance.getErrorRate() >= removalConfig.getErrorRate() && canRemovalNum >= 1
                    && instance.getRemovalStatus().compareAndSet(false, true)) {
                instance.setRemovalTime(System.currentTimeMillis());
                instance.setRecoveryTime(System.currentTimeMillis() + removalConfig.getRecoveryTime());
                //TODO 隔离后需要通知服务端更新状态，应该还需要修改注册中心服务状态
            } else {
                //正常server
                result.add(server);
            }
        }
        return result;
    }

    private double getCanRemovalNum(List<Server> instanceList, RemovalConfig removalConfig) {
        int removalCount = instanceList.size();
        return Math.min(instanceList.size() * removalConfig.getScaleUpLimit() - removalCount,
                instanceList.size() - removalCount - removalConfig.getMinInstanceNum());
    }

}
