package edu.swe2.cs.eventbus;

import java.util.List;

public interface IEventBus {

    void register(ISubscriber subscriber);

    void fire(IEvent<?> event);

    List<ISubscriber> getSubscribers();

}
