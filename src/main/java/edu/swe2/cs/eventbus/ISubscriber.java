package edu.swe2.cs.eventbus;

import java.util.Set;

public interface ISubscriber {

    /**
     * When eventbus fires an event, this method will be called if this class is listening to event
     *
     * @param event Event containing data
     */
    void handle(IEvent<?> event);

    /**
     * Set to which event subscriber should listen
     *
     * @return Set of supported events
     */
    Set<Class<?>> supports();
}
