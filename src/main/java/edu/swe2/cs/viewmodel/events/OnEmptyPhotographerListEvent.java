package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;

public class OnEmptyPhotographerListEvent implements IEvent {
    @Override
    public Object getData() {
        return true;
    }
}
