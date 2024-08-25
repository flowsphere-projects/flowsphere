package com.flowsphere.agent.core.config;

import com.flowsphere.agent.core.config.yaml.YamlMethodParameterPointcutConfig;
import com.flowsphere.agent.core.config.yaml.YamlMethodPointcutConfig;
import com.flowsphere.agent.core.enums.MethodTypeEnum;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MethodMatcherConfigCreator {

    public static List<MethodMatcherConfig> create(Collection<YamlMethodPointcutConfig> yamlMethodPointcutConfigs) {
        if (Objects.isNull(yamlMethodPointcutConfigs)) {
            return new ArrayList<>();
        }
        List<MethodMatcherConfig> result = new ArrayList<>(yamlMethodPointcutConfigs.size());
        for (YamlMethodPointcutConfig yamlConfig : yamlMethodPointcutConfigs) {
            result.add(new MethodMatcherConfig(createPointcut(yamlConfig), yamlConfig.getInterceptorName(), yamlConfig.getType()));
        }
        return result;
    }


    private static ElementMatcher<? super MethodDescription> createPointcut(YamlMethodPointcutConfig yamlConfig) {
        if (MethodTypeEnum.INSTANT.getType().equals(yamlConfig.getType()) || MethodTypeEnum.STATIC.getType().equals(yamlConfig.getType())) {
            return addMethodParams(yamlConfig.getParameterPointcutConfigs(), ElementMatchers.named(yamlConfig.getMethodName()));
        }
        if (MethodTypeEnum.CONSTRUCTOR.getType().equals(yamlConfig.getType())) {
            return addMethodParams(yamlConfig.getParameterPointcutConfigs(), ElementMatchers.isConstructor());
        }
        return null;
    }


    private static ElementMatcher<? super MethodDescription> addMethodParams(Collection<YamlMethodParameterPointcutConfig> parameterPointcutConfigs, ElementMatcher.Junction<MethodDescription> matcher) {
        for (YamlMethodParameterPointcutConfig parameterPointcutConfig : parameterPointcutConfigs) {
            matcher.and(ElementMatchers.takesArgument(parameterPointcutConfig.getIndex(), ElementMatchers.named(parameterPointcutConfig.getType())));
        }
        return matcher;
    }

}
