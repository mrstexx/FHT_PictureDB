package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;
import java.util.Set;

public class EXIFViewModel implements ISubscriber {

    private Picture picture;
    private StringProperty cameraProperty = new SimpleStringProperty();
    private StringProperty lensProperty = new SimpleStringProperty();
    private StringProperty dateProperty = new SimpleStringProperty();
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public EXIFViewModel() {
        eventBus.register(this);
    }

    /**
     * @return Current picture of exif data
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * @return Camera property for EXIF Data
     */
    public StringProperty cameraProperty() {
        return cameraProperty;
    }

    /**
     * @return Lens property for EXIF Data
     */
    public StringProperty lensProperty() {
        return lensProperty;
    }

    /**
     * @return Date property for EXIF Data
     */
    public StringProperty dateProperty() {
        return dateProperty;
    }

    private void updateExifData() {
        Exif exifDate = PictureBL.getInstance().getExifToPicture(picture);
        cameraProperty.setValue(exifDate.getCamera());
        lensProperty.setValue(exifDate.getLens());
        dateProperty.setValue(exifDate.getCaptureDate().toString());
    }

    @Override
    public void handle(IEvent<?> event) {
        picture = (Picture) event.getData();
        updateExifData();
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureSelectEvent.class);
        return supportedEvents;
    }
}
