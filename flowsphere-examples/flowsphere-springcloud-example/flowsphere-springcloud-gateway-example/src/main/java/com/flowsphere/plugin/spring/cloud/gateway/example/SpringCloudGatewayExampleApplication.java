package com.flowsphere.plugin.spring.cloud.gateway.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.flowsphere")
@SpringBootApplication
public class SpringCloudGatewayExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayExampleApplication.class, args);
    }

}
