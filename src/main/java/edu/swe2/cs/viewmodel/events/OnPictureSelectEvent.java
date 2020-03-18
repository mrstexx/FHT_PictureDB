package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;

public class OnPictureSelectEvent implements IEvent {

    private String imageUrl;

    public OnPictureSelectEvent(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Object getData() {
        return imageUrl;
    }
}
