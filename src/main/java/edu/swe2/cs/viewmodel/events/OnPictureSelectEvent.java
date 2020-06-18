package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Picture;

public class OnPictureSelectEvent implements IEvent<Picture> {

    private final Picture picture;

    public OnPictureSelectEvent(Picture picture) {
        this.picture = picture;
    }

    @Override
    public Picture getData() {
        return picture;
    }
}
