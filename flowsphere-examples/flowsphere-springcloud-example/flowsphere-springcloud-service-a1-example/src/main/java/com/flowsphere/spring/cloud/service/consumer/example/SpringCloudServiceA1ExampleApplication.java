package com.flowsphere.spring.cloud.service.consumer.example;

import com.alibaba.csp.sentinel.init.InitExecutor;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Collections;

@EnableFeignClients(basePackages = "com.flowsphere")
@SpringBootApplication
public class SpringCloudServiceA1ExampleApplication {

    public static void main(String[] args) {
//        // 定义热点限流的规则，对第一个参数设置 qps 限流模式，阈值为5
        FlowRule rule = new FlowRule();
        rule.setResource("userResource");
        // 限流类型，qps
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置阈值
        rule.setCount(5);
        // 限制哪个调用方
        rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
        // 基于调用关系的流量控制
        rule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        // 流控策略
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        FlowRuleManager.loadRules(Collections.singletonList(rule));
        InitExecutor.doInit();
        SpringApplication.run(SpringCloudServiceA1ExampleApplication.class, args);
    }

}
