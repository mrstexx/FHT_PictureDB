package edu.swe2.cs.eventbus;

import java.util.Set;

public interface ISubscriber {

    void handle(IEvent<?> event);

    Set<Class<?>> supports();
}
