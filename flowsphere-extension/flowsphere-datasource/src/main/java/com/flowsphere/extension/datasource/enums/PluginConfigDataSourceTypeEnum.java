package com.flowsphere.extension.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PluginConfigDataSourceTypeEnum {

    LOCAL("local"),
    NACOS("nacos"),
    APOLLO("apollo");

    private String type;

}
