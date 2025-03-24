package com.thalossphere.extension.datasource.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SentinelConfig {

    private String dataId;

    private String groupId;

    private String ruleKey;

    /**
     * application
     */
    private String namespace;

    private boolean resourceLimitEnabled;

    private boolean circuitBreakerEnabled;

    private HttpApiLimitConfig httpApiLimitConfig;

    private MybatisApiLimitConfig mybatisApiLimitConfig;

    private Map<String, Object> limitReturnResult;


    @Data
    public static class HttpApiLimitConfig {

        private boolean allUrlLimitEnabled;

        private List<String> excludeLimitUrlList;

    }

    @Data
    public static class MybatisApiLimitConfig {

        private boolean allMethodLimitEnabled;

        private List<String> excludeLimitMethodList;

    }


}
