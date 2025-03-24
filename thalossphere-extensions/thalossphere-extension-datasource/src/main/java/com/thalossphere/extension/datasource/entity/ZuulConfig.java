package com.thalossphere.extension.datasource.entity;

import com.thalossphere.common.loadbalance.RegionWeight;
import com.thalossphere.common.loadbalance.UserWeight;
import lombok.Data;

@Data
public class ZuulConfig {

    private RegionWeight regionWeight;

    private UserWeight userWeight;

}
