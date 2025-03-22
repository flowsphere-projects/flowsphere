package com.thalossphere.common.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusManager {

    private static final EventBus INSTANCE = new EventBus();

    private EventBusManager() {

    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public static void register(Object listener) {
        INSTANCE.register(listener);
    }

    public static void unregister(Object listener) {
        INSTANCE.unregister(listener);
    }

    public static void post(Object event) {
        INSTANCE.post(event);
    }

}
