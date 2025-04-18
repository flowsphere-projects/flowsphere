package com.thalossphere.common.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class YamlAgentConfig {

    private PluginConfigDataSource pluginConfigDataSource;

    private List<String> plugins = new ArrayList<>();

    private boolean sentinelEnabled;

    private OkHttpConfig okHttpConfig = new OkHttpConfig();

    private long longPollDelay = 3010;

    private String serverAddr;

    private boolean warmupEnabled;

    private boolean discoveryBinderEnabled;

}
