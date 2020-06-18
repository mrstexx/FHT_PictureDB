package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Photographer;

public class OnPhotographerSelectEvent implements IEvent<Photographer> {

    private final Photographer photographer;

    public OnPhotographerSelectEvent(Photographer photographer) {
        this.photographer = photographer;
    }


    @Override
    public Photographer getData() {
        return photographer;
    }
}
