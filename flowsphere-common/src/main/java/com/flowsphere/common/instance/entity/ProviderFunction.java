package com.flowsphere.common.instance.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProviderFunction extends Provider {

    private String url;

}
