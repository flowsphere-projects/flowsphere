package com.flowsphere.spring.cloud.zuul.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class SpringCloudZuulExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZuulExampleApplication.class);
    }

}
