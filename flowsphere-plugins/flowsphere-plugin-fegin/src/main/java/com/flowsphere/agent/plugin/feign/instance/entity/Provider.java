package com.flowsphere.agent.plugin.feign.instance.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Provider {

    private String providerName;

    private String ip;

}
