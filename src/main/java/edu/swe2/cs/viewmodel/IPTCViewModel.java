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
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();
    private Picture picture;

    public IPTCViewModel() {
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

    public void saveData() {
        Iptc iptcData;
        if (this.picture.getIptc() == null) {
            iptcData = new Iptc();
        } else {
            iptcData = this.picture.getIptc();
        }
        iptcData.setTitle(titleProperty.getValue());
        iptcData.setCaption(captionProperty.getValue());
        iptcData.setCity(cityProperty.getValue());
        this.picture.setIptc(iptcData);
        PictureBL.getInstance().updateIptc(iptcData, this.picture.getFileName());
    }

    private void updateIptcData() {
        Iptc iptcData = this.picture.getIptc();
        if (iptcData != null) {
            titleProperty.setValue(iptcData.getTitle());
            captionProperty.setValue(iptcData.getCaption());
            cityProperty.setValue(iptcData.getCity());
        }
    }

    private void resetIptcValues() {
        titleProperty.setValue("");
        captionProperty.setValue("");
        cityProperty.setValue("");
    }

    @Override
    public void handle(IEvent<?> event) {
        picture = (Picture) event.getData();
        resetIptcValues();
        updateIptcData();
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureSelectEvent.class);
        return supportedEvents;
    }
}
