package com.flowsphere.agent.core.builder;

import com.flowsphere.agent.core.config.MethodMatcherConfig;
import com.flowsphere.agent.core.interceptor.MethodInterceptor;
import com.flowsphere.agent.core.interceptor.MethodInterceptorManager;
import com.flowsphere.agent.core.interceptor.template.ConstructorMethodInterceptorTemplate;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorTemplate;
import com.flowsphere.agent.core.interceptor.template.StaticMethodInterceptorTemplate;
import com.flowsphere.agent.core.utils.MethodTypeUtils;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PluginsMethodInterceptorBuilder implements InterceptorBuilder {

    private final List<MethodMatcherConfig> methodMatcherConfigs;

    private final TypeDescription typePointcut;

    private final ClassLoader classLoader;

    public PluginsMethodInterceptorBuilder(List<MethodMatcherConfig> methodMatcherConfigs, TypeDescription typePointcut, ClassLoader classLoader) {
        this.methodMatcherConfigs = methodMatcherConfigs;
        this.typePointcut = typePointcut;
        this.classLoader = classLoader;
    }

    @Override
    public DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder) {

        try {
            for (MethodDescription.InDefinedShape each : typePointcut.getDeclaredMethods()) {
                Map<String, List<MethodInterceptor>> methodInterceptorMap = new HashMap<>();

                for (MethodMatcherConfig methodMatcherConfig : methodMatcherConfigs) {
                    if (methodMatcherConfig.getPointcut().matches(each)) {
                        methodInterceptorMap.computeIfAbsent(methodMatcherConfig.getType(), key -> new LinkedList<>());
                        methodInterceptorMap.get(methodMatcherConfig.getType()).add(MethodInterceptorManager.getMethodInterceptor(methodMatcherConfig.getInterceptorName(), classLoader));
                    }
                }

                if (methodInterceptorMap.isEmpty()) {
                    continue;
                }

                if (MethodTypeUtils.isConstructorMethod(each)) {
                    builder = new ConstructorMethodInterceptorTemplate(convert(methodInterceptorMap)).intercept(builder, each);
                    continue;
                }

                if (MethodTypeUtils.isInstantMethod(each)) {
                    builder = new InstantMethodInterceptorTemplate(convert(methodInterceptorMap)).intercept(builder, each);
                    continue;
                }

                if (MethodTypeUtils.isStaticMethod(each)) {
                    builder = new StaticMethodInterceptorTemplate(convert(methodInterceptorMap)).intercept(builder, each);
                }

            }
        } catch (Throwable e) {
            log.error("[flowsphere] init interceptor error", e);
        }
        return builder;
    }

    private <T extends MethodInterceptor> Map<String, List<T>> convert(final Map<String, List<MethodInterceptor>> advices) {
        Map<String, List<T>> result = new HashMap<>(advices.size(), 1F);
        for (Map.Entry<String, List<MethodInterceptor>> entry : advices.entrySet()) {
            result.put(entry.getKey(), new LinkedList<>());
            for (MethodInterceptor each : entry.getValue()) {
                result.get(entry.getKey()).add((T) each);
            }
        }
        return result;
    }


}
