package com.flowsphere.extension.datasource.entity;
import com.flowsphere.common.utils.JacksonUtils;
import com.google.common.collect.Lists;

import lombok.Data;

import java.util.List;

@Data
public class RemovalConfig {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 最小实例数
     */
    private int minInstanceNum;

    /**
     * 错误率
     */
    private double errorRate;

    /**
     * 恢复时长
     */
    private long recoveryTime;

    /**
     * 支持异常类型
     */
    private List<String> exceptions;

    /**
     * 支持http状态码
     */
    private List<Integer> httpStatus;

    /**
     * Remove the upper limit of the instance ratio
     */
    private double scaleUpLimit;

    /**
     * 窗口时间
     */
    private int windowsTime;

}
