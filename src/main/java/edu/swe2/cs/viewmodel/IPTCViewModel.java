package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;
import java.util.Set;

public class IPTCViewModel implements ISubscriber {

    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty captionProperty = new SimpleStringProperty();
    private StringProperty cityProperty = new SimpleStringProperty();
    private BooleanProperty disabledProperty = new SimpleBooleanProperty(true);
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();
    private Picture picture;

    public IPTCViewModel() {
        titleProperty.addListener((observable, oldValue, newValue) -> validate());
        captionProperty.addListener((observable, oldValue, newValue) -> validate());
        cityProperty.addListener((observable, oldValue, newValue) -> validate());
        eventBus.register(this);
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public StringProperty captionProperty() {
        return captionProperty;
    }

    public StringProperty cityProperty() {
        return cityProperty;
    }

    public BooleanProperty disabledProperty() {
        return disabledProperty;
    }

    private boolean validateTitleProp() {
        if (titleProperty.get() == null) {
            return true;
        }
        return titleProperty.get().isEmpty();
    }

    private boolean validateCaptionProp() {
        if (captionProperty.get() == null) {
            return true;
        }
        return captionProperty.get().isEmpty();
    }

    private boolean validateCityProp() {
        if (cityProperty.get() == null) {
            return true;
        }
        return cityProperty.get().isEmpty();
    }

    private void validate() {
        if (validateTitleProp() && validateCaptionProp() && validateCityProp()) {
            disabledProperty.setValue(true);
        } else {
            disabledProperty.setValue(false);
        }
    }

    public void saveData() {
        Iptc iptcData = this.picture.getIptc();
        // TODO Stefan: addIptc missing
        if (iptcData == null) {
            return;
        }
        if (!validateTitleProp()) {
            iptcData.setTitle(titleProperty.getValue());
        }
        if (!validateCaptionProp()) {
            iptcData.setCaption(captionProperty.getValue());
        }
        if (!validateCityProp()) {
            iptcData.setCity(cityProperty.getValue());
        }
        this.picture.setIptc(iptcData);
        PictureBL.getInstance().savePicture(this.picture);
    }

    private void updateIptcData() {
        Iptc iptcData = this.picture.getIptc();
        if (iptcData != null) {
            titleProperty.setValue(iptcData.getTitle());
            captionProperty.setValue(iptcData.getCaption());
            cityProperty.setValue(iptcData.getCity());
        }
    }

    @Override
    public void handle(IEvent<?> event) {
        picture = (Picture) event.getData();
        updateIptcData();
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureSelectEvent.class);
        return supportedEvents;
    }
}
