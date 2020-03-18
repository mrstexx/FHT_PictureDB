package edu.swe2.cs.eventbus;

import java.util.ArrayList;
import java.util.List;

public class SharedEventBus implements IEventBus {

    List<ISubscriber> subscribers;

    public SharedEventBus() {
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void register(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void fire(IEvent<?> event) {
        subscribers.stream()
                .filter(subscriber -> subscriber.supports().contains(event.getClass()))
                .forEach(subscriber -> subscriber.handle(event));
    }

    @Override
    public List<ISubscriber> getSubscribers() {
        return subscribers;
    }
}
