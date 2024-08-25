package com.flowsphere.elasticjob.example.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MyJob implements SimpleJob {

    public void execute(ShardingContext shardingContext) {
        System.out.println("MyJob执行中");
    }

}

