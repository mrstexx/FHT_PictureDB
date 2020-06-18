package edu.swe2.cs.eventbus;

import java.util.List;

public interface IEventBus {

    /**
     * It is used to register a subscriber, otherwise subscriber will not be able to receive an event.
     *
     * @param subscriber Subscriber to be registered.
     */
    void register(ISubscriber subscriber);

    /**
     * When fired, event will be to sent to registered subscribers
     *
     * @param event Event to be sent on subscribers
     */
    void fire(IEvent<?> event);

    /**
     * @return Get list of all subscribers
     */
    List<ISubscriber> getSubscribers();

}
