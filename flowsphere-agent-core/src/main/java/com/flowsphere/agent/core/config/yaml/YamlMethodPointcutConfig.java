package com.flowsphere.agent.core.config.yaml;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;

@Data
public class YamlMethodPointcutConfig {

    private String methodName;

    private String type;

    private String interceptorName;

    private Collection<YamlMethodParameterPointcutConfig> parameterPointcutConfigs = new LinkedList<>();

}
