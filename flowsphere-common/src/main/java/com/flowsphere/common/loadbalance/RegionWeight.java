package com.flowsphere.common.loadbalance;

import lombok.Data;

import java.util.List;

@Data
public class RegionWeight {

    private List<String> regions;

    private List<TagWeight> tagWeights;

}
