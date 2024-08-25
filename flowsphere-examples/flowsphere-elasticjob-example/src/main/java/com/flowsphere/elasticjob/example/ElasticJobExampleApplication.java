package com.flowsphere.elasticjob.example;

import com.flowsphere.elasticjob.example.config.EmbedZookeeperServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticJobExampleApplication {

    public static void main(String[] args) {
        EmbedZookeeperServer.start(2181);
        SpringApplication.run(ElasticJobExampleApplication.class, args);
    }

}
