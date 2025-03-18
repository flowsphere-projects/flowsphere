package com.flowsphere.common.eventbus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CmdEnum {

    PROVIDER_OFFLINE("PROVIDER_OFFLINE","服务下线");

    private String cmd;

    private String desc;

}
