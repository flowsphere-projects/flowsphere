package com.flowsphere.common.loadbalance;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagWeight extends ArrayWeight {

    private String tag;

    public TagWeight(String tag, double weight) {
        this.tag = tag;
        super.setWeight(weight);
    }

    @Override
    public Object getObj() {
        return tag;
    }
}
