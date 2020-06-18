package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;

public class OnEmptyPhotographerListEvent implements IEvent<Boolean> {
    @Override
    public Boolean getData() {
        return true;
    }
}
