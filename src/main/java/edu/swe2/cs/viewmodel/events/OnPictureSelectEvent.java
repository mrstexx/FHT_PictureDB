package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Picture;

public class OnPictureSelectEvent implements IEvent {

    private Picture picture;

    public OnPictureSelectEvent(Picture picture) {
        this.picture = picture;
    }

    @Override
    public Object getData() {
        return picture;
    }
}
