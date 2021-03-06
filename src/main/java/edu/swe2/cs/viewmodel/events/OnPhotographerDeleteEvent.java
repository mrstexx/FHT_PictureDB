package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Photographer;

public class OnPhotographerDeleteEvent implements IEvent<Photographer> {

    private final Photographer photographer;

    public OnPhotographerDeleteEvent(Photographer photographer) {
        this.photographer = photographer;
    }

    @Override
    public Photographer getData() {
        return photographer;
    }
}
