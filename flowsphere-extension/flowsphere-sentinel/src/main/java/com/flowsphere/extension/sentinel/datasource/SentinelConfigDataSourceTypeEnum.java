package com.flowsphere.extension.sentinel.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SentinelConfigDataSourceTypeEnum {

    LOCAL("local"),
    NACOS("nacos"),
    APOLLO("apollo");

    private String type;

}
