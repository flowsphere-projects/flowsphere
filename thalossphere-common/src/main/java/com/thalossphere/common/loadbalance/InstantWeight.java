package com.thalossphere.common.loadbalance;

import lombok.Data;

@Data
public class InstantWeight {

    private RegionWeight regionWeight;

    private UserWeight userWeight;


}
