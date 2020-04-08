package edu.swe2.cs.viewmodel.events;

import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.model.Picture;

import java.util.List;

public class OnSearchPicturesEvent implements IEvent {

    private List<Picture> pictures;

    public OnSearchPicturesEvent(List<Picture> pictures){
        this.pictures = pictures;
    }

    @Override
    public Object getData() {
        return this.pictures;
    }
}
