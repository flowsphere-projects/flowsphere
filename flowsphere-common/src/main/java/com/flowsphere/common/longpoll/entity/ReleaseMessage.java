package com.flowsphere.common.longpoll.entity;

import lombok.Data;

@Data
public class ReleaseMessage {

    private String cmd;

    private String extendData;

    private String applicationName;

}
