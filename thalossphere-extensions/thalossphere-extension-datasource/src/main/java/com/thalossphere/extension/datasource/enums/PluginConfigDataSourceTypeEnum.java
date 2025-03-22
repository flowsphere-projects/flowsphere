package com.thalossphere.extension.datasource.enums;

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
