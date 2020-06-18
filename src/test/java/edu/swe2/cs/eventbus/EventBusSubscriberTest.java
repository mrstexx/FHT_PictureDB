package edu.swe2.cs.eventbus;

import java.util.HashSet;
import java.util.Set;

public class EventBusSubscriberTest implements ISubscriber {

    private boolean value;

    public boolean isValue() {
        return value;
    }

    @Override
    public void handle(IEvent<?> event) {
        value = (Boolean) event.getData();
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnEventBusTestA.class);
        supportedEvents.add(OnEventBusTestB.class);
        return supportedEvents;
    }

}
