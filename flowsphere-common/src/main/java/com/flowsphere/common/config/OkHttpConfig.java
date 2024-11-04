package com.flowsphere.common.config;

import lombok.Data;

@Data
public class OkHttpConfig {

    private long connectTimeout = 30;

    private long writeTimeout = 30;

    private long readTimeout = 30;

}