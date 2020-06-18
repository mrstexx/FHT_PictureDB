package edu.swe2.cs.eventbus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SharedEventBus implements IEventBus {

    private static final Logger LOG = LogManager.getLogger(SharedEventBus.class);
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
        LOG.info("Firing an event '" + event.getClass().getName() + "'");
        subscribers.stream()
                .filter(subscriber -> subscriber.supports().contains(event.getClass()))
                .forEach(subscriber -> subscriber.handle(event));
    }

    @Override
    public List<ISubscriber> getSubscribers() {
        return subscribers;
    }
}
