package com.flowsphere.feature.discovery.binder.event;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InstanceInitEvent {

    private String applicationName;

    private String serverAddr;

}
