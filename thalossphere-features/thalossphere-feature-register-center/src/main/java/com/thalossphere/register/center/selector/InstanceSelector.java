package com.thalossphere.register.center.selector;

import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.loadbalance.ArrayWeightRandom;
import com.thalossphere.feature.removal.RemovalInstanceService;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class InstanceSelector {
    private static final int DEFAULT_WARMUP = 10 * 60 * 1000;

    public List<Server> selectInstances(List<Server> servers) {
        List<Server> filteredServers = filterServersByCondition(servers);
        return applyWeightedLoadBalancing(filteredServers);
    }

    public List<Server> filterServersByCondition(List<Server> servers) {
        List<Server> validServers = new ArrayList<>();
        for (Server server : servers) {
            if (isValidServer(server)) {
                validServers.add(server);
            }
        }
        List<Server> finalFilteredServers = RemovalInstanceService.getInstance().removal(validServers.isEmpty() ? servers : validServers);
        return finalFilteredServers.isEmpty() ? servers : finalFilteredServers;
    }


    public List<Server> applyWeightedLoadBalancing(List<Server> servers) {
        YamlAgentConfig config = YamlAgentConfigCache.get();
        if (!config.isWarmupEnabled() || servers.size() == 1) {
            return servers;
        }
        List<ServerWeight> weightedServers = new ArrayList<>();
        for (Server server : servers) {
            Map<String, String> metadata = getMetadata(server);
            weightedServers.add(new ServerWeight(server, computeServerWeight(metadata)));
        }
        ArrayWeightRandom<ServerWeight> weightedRandomSelector = new ArrayWeightRandom<>(weightedServers);
        return Collections.singletonList((Server) weightedRandomSelector.choose());
    }

    public int computeServerWeight(Map<String, String> metadata) {
        int weight = Integer.parseInt(metadata.getOrDefault("weight", "100"));
        if (weight > 0) {
            long startTime = Long.parseLong(metadata.getOrDefault("timestamp", String.valueOf(System.currentTimeMillis())));
            if (startTime > 0) {
                long uptime = System.currentTimeMillis() - startTime;
                if (uptime < 0) return 1;
                int warmupDuration = Integer.parseInt(metadata.getOrDefault("warmup", String.valueOf(DEFAULT_WARMUP)));
                if (uptime < warmupDuration) {
                    return computeWarmupAdjustedWeight((int) uptime, warmupDuration, weight);
                }
            }
        }
        return Math.max(weight, 0);
    }

    public static int computeWarmupAdjustedWeight(int uptime, int warmupDuration, int maxWeight) {
        int adjustedWeight = (int) (uptime / ((float) warmupDuration / maxWeight));
        return Math.max(1, Math.min(adjustedWeight, maxWeight));
    }

    public abstract Map<String, String> getMetadata(Server server);

    public abstract boolean isValidServer(Server server);


}
