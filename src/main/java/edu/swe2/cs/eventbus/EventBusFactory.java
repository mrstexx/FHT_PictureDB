package edu.swe2.cs.eventbus;

public class EventBusFactory {

    public static IEventBus createSharedEventBus() {
        return EventBusHolder.SHARED_BUS.getEventBus();
    }

}
