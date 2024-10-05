package com.flowsphere.common.instance.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Consumer implements Serializable {

    private String applicationName;

    private Map<String, List<String>> dependOnInterfaceList;

}
