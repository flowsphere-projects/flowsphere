package com.flowsphere.spring.cloud.service.provider.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.flowsphere")
@SpringBootApplication
public class SpringCloudServiceB1ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServiceB1ExampleApplication.class, args);
    }

}
