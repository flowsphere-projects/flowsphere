package com.thalossphere.spring.boot2;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.env.Env;
import com.thalossphere.common.transport.SimpleHttpClient;
import com.thalossphere.common.transport.SimpleHttpRequest;
import com.thalossphere.common.utils.NetUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.thalossphere.common.constant.CommonConstant.SERVER_PORT;
import static com.thalossphere.common.constant.CommonConstant.SPRING_APPLICATION_NAME;

@Slf4j
public class AppShutdownInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        offline(Env.get(SPRING_APPLICATION_NAME), NetUtils.getIpAddress(), Integer.parseInt(Env.get(SERVER_PORT)));
    }

    public void offline(String applicationName, String ip, int port) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("ip", ip);
            param.put("applicationName", applicationName);
            param.put("port", port);
            responseHandler(SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/offline")
                    .setData(param)));
        } catch (Exception e) {
            log.error("[thalossphere] register provider instant removal fail {}", ip, e);
        }
    }

    private String getServerAddr() {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        return yamlAgentConfig.getServerAddr();
    }

    private void responseHandler(Response response) {
        if (!response.isSuccessful()) {
            throw new RuntimeException("provider offline error");
        }
    }

}
