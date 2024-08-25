package com.flowsphere.agent.core.config.yaml;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class YamlPointcutConfig {

    private List<YamlClassPointcutConfig> pointcutConfigs = new LinkedList<>();

}
