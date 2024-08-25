package com.flowsphere.extension.datasource.entity;

import com.flowsphere.common.loadbalance.RegionWeight;
import com.flowsphere.common.loadbalance.UserWeight;
import lombok.Data;

@Data
public class ZuulConfig {

    private RegionWeight regionWeight;

    private UserWeight userWeight;

}
