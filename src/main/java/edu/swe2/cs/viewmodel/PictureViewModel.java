package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.URLBuilder;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class PictureViewModel implements ISubscriber {

    private Picture picture;
    private ObjectProperty<Image> currentPicture;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public PictureViewModel() {
        eventBus.register(this);
    }

    /**
     * Set current picture
     *
     * @param picture Picture to be set as current
     */
    public void setPicture(Picture picture) {
        this.picture = picture;
        PictureBL.getInstance().setCurrentPicture(picture);
    }

    /**
     * @return Current selected picture
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * @return Property for the current picture
     */
    public ObjectProperty<Image> getCurrentPicture() {
        if (picture == null) {
            currentPicture = new SimpleObjectProperty<>();
        } else if (currentPicture == null) {
            Image img = new Image(getPicturePath());
            currentPicture = new SimpleObjectProperty<>(img);
        }
        return currentPicture;
    }

    /**
     * @return Picture path for Image Node element. It starts with "file:"
     */
    public String getPicturePath() {
        if (picture == null) {
            return "";
        }
        return URLBuilder.getPreparedImgPath(picture.getFileName());
    }

    @Override
    public void handle(IEvent<?> event) {
        setPicture((Picture) event.getData());
        currentPicture.setValue(new Image(getPicturePath()));
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureSelectEvent.class);
        return supportedEvents;
    }

    /**
     * @param event Mouse event on click
     * @return true if double click
     */
    public boolean isOnImageClick(MouseEvent event) {
        return event.getClickCount() == 2;
    }
}
