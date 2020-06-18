package edu.swe2.cs.eventbus;

public enum EventBusHolder {
    SHARED_BUS(new SharedEventBus());

    private final IEventBus eventBus;

    EventBusHolder(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public IEventBus getEventBus() {
        return this.eventBus;
    }
}
