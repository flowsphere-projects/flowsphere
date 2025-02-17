package com.flowsphere.common.loadbalance;

import lombok.Data;

@Data
public abstract class ArrayWeight {

    private double weight;

    public abstract Object getObj();

}
