package com.thalossphere.agent.core;

import com.thalossphere.agent.core.builder.InterceptorBuilderChain;
import com.thalossphere.agent.core.builder.MultiThreadMethodInterceptorBuilder;
import com.thalossphere.agent.core.builder.PluginsMethodInterceptorBuilder;
import com.thalossphere.agent.core.builder.TargetObjectInterceptorBuilder;
import com.thalossphere.agent.core.config.MethodMatcherConfigCreator;
import com.thalossphere.agent.core.config.yaml.YamlMethodPointcutConfig;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class AgentTransform implements AgentBuilder.Transformer {

    private final Map<String, Collection<YamlMethodPointcutConfig>> classPointcutConfigMap;

    private final ClassLoader classLoader;

    public AgentTransform(Map<String, Collection<YamlMethodPointcutConfig>> classPointcutConfigMap, ClassLoader classLoader) {
        this.classPointcutConfigMap = classPointcutConfigMap;
        this.classLoader = classLoader;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader currentClassLoader, JavaModule javaModule, ProtectionDomain protectionDomain) {
        try {
            log.info("[thalossphere] transform load interceptor typeName={}", typeDescription.getTypeName());
            Collection<YamlMethodPointcutConfig> methodPointcutConfigs = classPointcutConfigMap.get(typeDescription.getTypeName());
            return InterceptorBuilderChain.buildInterceptor(builder, new TargetObjectInterceptorBuilder(),
                    new PluginsMethodInterceptorBuilder(MethodMatcherConfigCreator.create(methodPointcutConfigs), typeDescription, classLoader), new MultiThreadMethodInterceptorBuilder());
        } catch (Throwable e) {
            log.error("[thalossphere] transform execute error", e);
            return builder;
        }
    }
}
