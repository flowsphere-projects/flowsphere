package com.flowsphere.common.loadbalance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagWeight {

    private String tag;

    private double weight;

}
