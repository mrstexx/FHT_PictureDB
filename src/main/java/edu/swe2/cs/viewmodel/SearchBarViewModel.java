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

    /**
     * @return Search property for text to be entered
     */
    public StringProperty searchTextProperty() {
        return searchText;
    }

    /**
     * Search and update picture list view of string property value
     */
    public void search() {
        List<Picture> pictureList;
        if (isEmpty()) {
            // get all pictures if empty search
            pictureList = PictureBL.getInstance().getAllPictures();
        } else {
            pictureList = PictureBL.getInstance().getPicturesToSearch(searchText.getValue());
        }
        eventBus.fire(new OnSearchPicturesEvent(pictureList));
    }

    public boolean isEmpty() {
        return searchText.getValue() == null || searchText.getValue().isEmpty();
    }

}
