package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Picture;

public class OnPictureDoubleClickEvent implements IEvent<Picture> {

    private final Picture picture;

    public OnPictureDoubleClickEvent(Picture picture) {
        this.picture = picture;
    }

    @Override
    public Picture getData() {
        return picture;
    }
}
