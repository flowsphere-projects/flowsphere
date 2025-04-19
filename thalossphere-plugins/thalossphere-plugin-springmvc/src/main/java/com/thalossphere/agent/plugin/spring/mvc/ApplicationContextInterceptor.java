package com.thalossphere.agent.plugin.spring.mvc;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.eventbus.EventBusManager;
import com.thalossphere.common.utils.StringUtils;
import com.thalossphere.feature.discovery.binder.event.InstanceInitEvent;
import com.thalossphere.feature.discovery.binder.listener.InstanceInitListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.thalossphere.common.constant.CommonConstant.SERVER_PORT;
import static com.thalossphere.common.constant.CommonConstant.SPRING_APPLICATION_NAME;

@Slf4j
public class ApplicationContextInterceptor implements InstanceMethodInterceptor {

    private static final AtomicBoolean STARTER = new AtomicBoolean(false);

    private static final String INSTANT_INIT_ENABLED = "thalossphere.instantInitEnabled";

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) allArguments[0];
        ConfigurableEnvironment environment = context.getEnvironment();
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        String serverAddr = getServerAddr();
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        if (StringUtils.isNotEmpty(applicationName) && StringUtils.isNotEmpty(serverAddr) && yamlAgentConfig.isDiscoveryBinderEnabled()) {
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
