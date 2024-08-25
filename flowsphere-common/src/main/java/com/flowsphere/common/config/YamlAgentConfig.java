package com.flowsphere.common.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class YamlAgentConfig {

    private PluginConfigDataSource pluginConfigDataSource;

    private List<String> plugins = new ArrayList<>();

    private boolean sentinelEnabled;

}
