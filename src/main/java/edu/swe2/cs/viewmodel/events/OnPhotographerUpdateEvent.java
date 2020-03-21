package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Photographer;

public class OnPhotographerUpdateEvent implements IEvent {

    private Photographer photographer;

    public OnPhotographerUpdateEvent(Photographer photographer) {
        this.photographer = photographer;
    }

    @Override
    public Object getData() {
        return photographer;
    }
}
