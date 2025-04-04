package com.thalossphere.plugin.spring.cloud.gateway.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.thalossphere")
@SpringBootApplication
public class SpringCloudGatewayExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayExampleApplication.class, args);
    }

}
