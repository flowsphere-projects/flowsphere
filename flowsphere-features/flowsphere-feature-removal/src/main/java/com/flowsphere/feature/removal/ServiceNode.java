package com.flowsphere.feature.removal;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ServiceNode {

    /**
     * 请求数
     */
    private AtomicInteger requestNum = new AtomicInteger(0);

    /**
     * 失败请求数
     */
    private AtomicInteger requestFailNum = new AtomicInteger(0);

    /**
     * 隔离状态
     */
    private AtomicBoolean removalStatus = new AtomicBoolean(false);

    /**
     * 隔离时间
     */
    private long removalTime;

    /**
     * 恢复时间
     */
    private long recoveryTime;

    /**
     * host地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 最后调用时间
     */
    private long lastInvokeTime;

    /**
     * 错误率
     */
    private double errorRate;

}
