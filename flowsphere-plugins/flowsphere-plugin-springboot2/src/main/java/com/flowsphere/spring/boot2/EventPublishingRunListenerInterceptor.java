package com.flowsphere.spring.boot2;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class EventPublishingRunListenerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) allArguments[0];
        Map<String, String> propertiesMap = getProperties(context);
        Map<String, String> loosePropertiesMap = generatorLooseProperties(propertiesMap);
        Env.putAll(loosePropertiesMap);
    }

    private Map<String, String> generatorLooseProperties(Map<String, String> propertiesMap) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            String newKey = StringUtils.convertToPropertyName(entry.getKey());
            result.put(newKey, entry.getValue());
        }
        return result;
    }


    private Map<String, String> getProperties(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        return StreamSupport.stream(propertySources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), environment::getProperty));
    }

}
