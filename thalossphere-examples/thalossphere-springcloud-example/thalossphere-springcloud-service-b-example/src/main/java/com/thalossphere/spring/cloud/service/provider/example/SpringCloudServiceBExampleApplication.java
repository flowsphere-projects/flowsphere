package com.thalossphere.spring.cloud.service.provider.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.thalossphere")
@SpringBootApplication
public class SpringCloudServiceBExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServiceBExampleApplication.class, args);
    }

}
