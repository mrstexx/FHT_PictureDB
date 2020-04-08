package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnSearchPicturesEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class SearchBarViewModel {

    private StringProperty searchText = new SimpleStringProperty();
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public SearchBarViewModel() {
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }

    public void search() {
        List<Picture> pictureList = new ArrayList<>();
        if (isEmpty()) {
            // get all pictures if empty search
            pictureList = PictureBL.getInstance().getAllPictures();
        } else {
            pictureList = PictureBL.getInstance().getPicturesToSearch(searchText.getValue());
        }
        eventBus.fire(new OnSearchPicturesEvent(pictureList));
    }

    public boolean isEmpty() {
        return searchText.getValue().isEmpty();
    }

}
