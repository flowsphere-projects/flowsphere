//package com.thalossphere.test.plugin.consul;
//
//import com.ecwid.consul.v1.health.model.Check;
//import com.ecwid.consul.v1.health.model.HealthService;
//import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
//import com.thalossphere.agent.plugin.consul.CompositePredicateInterceptor;
//import com.thalossphere.common.config.YamlAgentConfig;
//import com.thalossphere.common.config.YamlAgentConfigCache;
//import com.thalossphere.common.constant.CommonConstant;
//import com.thalossphere.common.env.Env;
//import com.thalossphere.common.tag.context.TagManager;
//import com.netflix.loadbalancer.Server;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.cloud.consul.discovery.ConsulServer;
//
//import java.util.*;
//
//public class CompositePredicateInterceptorTest {
//
//    @Test
//    public void beforeTest() {
//        Object[] objects = new Object[]{buildServerList()};
//        System.setProperty("thalossphere.tag", "TAGA");
//        initEnv();
//        initWarmupConfig();
//        CompositePredicateInterceptor interceptor = new CompositePredicateInterceptor();
//        InstantMethodInterceptorResult instantMethodInterceptorResult = new InstantMethodInterceptorResult();
//        interceptor.beforeMethod(null, objects,
//                null, null, instantMethodInterceptorResult);
//        Assertions.assertTrue(!instantMethodInterceptorResult.isContinue());
//        Assertions.assertTrue(((List<Server>) instantMethodInterceptorResult.getResult()).size() == 1);
//    }
//
//    private void initWarmupConfig() {
//        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
//        yamlAgentConfig.setWarmupEnabled(true);
//        YamlAgentConfigCache.put(yamlAgentConfig);
//    }
//
//    private void initEnv() {
//        HashMap<String, String> configMap = new HashMap<>();
//        configMap.put("spring.cloud.consul.host", "127.0.0.1");
//        Env.putAll(configMap);
//    }
//
//    @SneakyThrows
//    private List<Server> buildServerList() {
//        List<Server> serverList = new ArrayList<>();
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("tag", "TAGA");
//        HealthService healthService = new HealthService();
//        HealthService.Service service = new HealthService.Service();
//        service.setTags(Arrays.asList(CommonConstant.SERVER_TAG + "=" + TagManager.getSystemTag()));
//        service.setPort(8500);
//        service.setAddress("127.0.0.1");
//        service.setId("consul");
//        Check check = new Check();
//        check.setStatus(Check.CheckStatus.PASSING);
//        healthService.setChecks(Arrays.asList(check));
//        healthService.setService(service);
//        ConsulServer consulServer = new ConsulServer(healthService);
//        serverList.add(consulServer);
//        return serverList;
//    }
//
//}
