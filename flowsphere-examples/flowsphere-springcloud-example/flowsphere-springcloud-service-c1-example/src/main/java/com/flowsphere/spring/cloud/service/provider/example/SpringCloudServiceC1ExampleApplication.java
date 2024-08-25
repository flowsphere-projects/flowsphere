package com.flowsphere.spring.cloud.service.provider.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SpringCloudServiceC1ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServiceC1ExampleApplication.class, args);
    }

}
