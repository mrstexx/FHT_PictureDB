package edu.swe2.cs.viewmodel;

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
    private StringProperty camera;
    private StringProperty lens;
    private StringProperty date;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public EXIFViewModel() {
        eventBus.register(this);
    }

    public StringProperty getCamera() {
        if (camera == null) {
            camera = new SimpleStringProperty();
        }
        return camera;
    }

    public StringProperty getLens() {
        if (lens == null) {
            lens = new SimpleStringProperty();
        }
        return lens;
    }

    public StringProperty getDate() {
        if (date == null) {
            date = new SimpleStringProperty();
        }
        return date;
    }

    private void updateExifData() {
        Exif exifDate = picture.getExif();
        camera.setValue(exifDate.getCamera());
        lens.setValue(exifDate.getLens());
        date.setValue(exifDate.getCaptureDate().toString());
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
