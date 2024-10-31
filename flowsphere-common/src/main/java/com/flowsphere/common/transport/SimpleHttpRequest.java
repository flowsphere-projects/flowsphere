package com.flowsphere.common.transport;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SimpleHttpRequest implements Serializable {

    private String url;

    private Object data;

}
