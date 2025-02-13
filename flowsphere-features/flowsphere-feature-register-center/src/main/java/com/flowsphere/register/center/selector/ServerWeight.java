package com.flowsphere.register.center.selector;

import com.flowsphere.common.loadbalance.ArrayWeight;
import com.netflix.loadbalancer.Server;
import lombok.Data;

@Data
public class ServerWeight extends ArrayWeight {

    private Server server;

    public ServerWeight(Server server, double weight) {
        this.server = server;
        super.setWeight(weight);
    }

    @Override
    public Object getObj() {
        return server;
    }
}
