package com.flowsphere.agent.plugin.spring.mvc;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.config.YamlAgentConfigCache;
import com.flowsphere.common.eventbus.EventBusManager;
import com.flowsphere.common.utils.StringUtils;
import com.flowsphere.feature.discovery.binder.event.InstanceInitEvent;
import com.flowsphere.feature.discovery.binder.listener.InstanceInitListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.flowsphere.common.constant.CommonConstant.SERVER_PORT;
import static com.flowsphere.common.constant.CommonConstant.SPRING_APPLICATION_NAME;

@Slf4j
public class ApplicationContextInterceptor implements InstantMethodInterceptor {

    private static final AtomicBoolean STARTER = new AtomicBoolean(false);

    private static final String INSTANT_INIT_ENABLED = "flowsphere.instantInitEnabled";

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) allArguments[0];
        ConfigurableEnvironment environment = context.getEnvironment();
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        String serverAddr = getServerAddr();
        Boolean instantInitEnabled = Boolean.valueOf(environment.getProperty(INSTANT_INIT_ENABLED));
        if (StringUtils.isNotEmpty(applicationName) && StringUtils.isNotEmpty(serverAddr) && instantInitEnabled) {
            if (STARTER.compareAndSet(false, true)) {
                int port = Integer.parseInt(environment.getProperty(SERVER_PORT));
                EventBusManager.getInstance().register(new InstanceInitListener());
                EventBusManager.getInstance().post(new InstanceInitEvent()
                        .setApplicationName(applicationName)
                        .setServerAddr(serverAddr)
                        .setPort(port));
            }
        }
    }


    private String getServerAddr() {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        return yamlAgentConfig.getServerAddr();
    }

}
