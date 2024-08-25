package com.flowsphere.agent.core.config.yaml;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;

@Data
public class YamlClassPointcutConfig {

    private String className;

    private Collection<YamlMethodPointcutConfig> methodPointcutConfigs = new LinkedList<>();



}
