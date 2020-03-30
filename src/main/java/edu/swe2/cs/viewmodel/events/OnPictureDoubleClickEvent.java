package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Picture;

public class OnPictureDoubleClickEvent implements IEvent {

    private Picture picture;

    public OnPictureDoubleClickEvent(Picture picture) {
        this.picture = picture;
    }

    @Override
    public Object getData() {
        return picture;
    }
}
