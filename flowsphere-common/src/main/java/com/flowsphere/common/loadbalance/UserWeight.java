package com.flowsphere.common.loadbalance;

import lombok.Data;

import java.util.List;

@Data
public class UserWeight {

    private List<String> userIds;

    private List<TagWeight> tagWeights;

}
