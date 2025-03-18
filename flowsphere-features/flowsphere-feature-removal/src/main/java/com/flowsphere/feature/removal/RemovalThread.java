package com.flowsphere.feature.removal;

import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.flowsphere.feature.discovery.binder.InstanceService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sun.webkit.graphics.GraphicsDecoder.SCALE;


@Slf4j
public class RemovalThread implements Runnable {


    @Override
    public void run() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return;
        }
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        if (instanceCallResult.isEmpty()) {
            return;
        }
        for (Iterator<Map.Entry<String, ServiceNode>> iterator = instanceCallResult.entrySet().iterator();
             iterator.hasNext(); ) {
            ServiceNode serviceNode = iterator.next().getValue();
            if (System.currentTimeMillis() - serviceNode.getLastInvokeTime() >= removalConfig.getRecoveryTime()) {
                reset(serviceNode);
                InstanceService.modifyProviderInstanceRemoval(serviceNode.getIp(), serviceNode.getPort(), 1);
                continue;
            }
            serviceNode.setErrorRate(calErrorRate(serviceNode));
            if (log.isDebugEnabled()) {
                log.info("[flowsphere] ServiceNode calculating error rate all serviceNode {}", serviceNode);
            }

        }
    }


    private void reset(ServiceNode serviceNode) {
        serviceNode.setRemovalTime(0);
        serviceNode.setRecoveryTime(0);
        serviceNode.setRequestFailNum(new AtomicInteger(0));
        serviceNode.setErrorRate(0);
        serviceNode.setRemovalStatus(new AtomicBoolean(false));
    }


    private float calErrorRate(ServiceNode info) {
        if (info.getRequestNum().get() == 0 || info.getRequestFailNum().get() == 0) {
            return 0;
        } else {
            BigDecimal count = new BigDecimal(info.getRequestNum().get());
            BigDecimal failNum = new BigDecimal(info.getRequestFailNum().get());
            return failNum.divide(count, SCALE, RoundingMode.HALF_UP).floatValue();
        }
    }


}
