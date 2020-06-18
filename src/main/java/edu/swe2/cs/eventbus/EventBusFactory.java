package edu.swe2.cs.eventbus;

public class EventBusFactory {

    /**
     * Create shared event bus that is allowed to be used for every components
     *
     * @return Implementation of shared event bus
     */
    public static IEventBus createSharedEventBus() {
        return EventBusHolder.SHARED_BUS.getEventBus();
    }

}
