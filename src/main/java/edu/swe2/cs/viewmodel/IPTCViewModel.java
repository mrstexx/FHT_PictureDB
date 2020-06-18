package edu.swe2.cs.viewmodel;

import com.itextpdf.text.pdf.StringUtils;
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
import java.util.List;
import java.util.Set;

public class IPTCViewModel implements ISubscriber {

    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty captionProperty = new SimpleStringProperty();
    private StringProperty cityProperty = new SimpleStringProperty();
    private StringProperty tagsProperty = new SimpleStringProperty();
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();
    private Picture picture;

    public IPTCViewModel() {
        eventBus.register(this);
    }

    public Picture getPicture() {
        return picture;
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

    public StringProperty tagsProperty() {
        return tagsProperty;
    }

    public void saveData() {
        Iptc iptcData;
        if (PictureBL.getInstance().getIptcToPicture(this.picture) == null) {
            iptcData = new Iptc();
        } else {
            iptcData = PictureBL.getInstance().getIptcToPicture(this.picture);
        }
        iptcData.setTitle(titleProperty.getValue());
        iptcData.setCaption(captionProperty.getValue());
        iptcData.setCity(cityProperty.getValue());
        addTagsToIptcData(iptcData);
        this.picture.setIptc(iptcData);
        PictureBL.getInstance().updateIptc(iptcData, this.picture);
    }

    private void addTagsToIptcData(Iptc iptc) {
        // always remove all from set and add new
        iptc.getAllTags().clear();
        // split tags by "," and add to set
        String[] tags = tagsProperty.getValue().split(",");
        for (String tag : tags) {
            String trimmedTag = tag.trim();
            if (!trimmedTag.isEmpty()) {
                iptc.addTag(trimmedTag);
            }
        }
    }

    private void updateIptcData() {
        Iptc iptcData = PictureBL.getInstance().getIptcToPicture(this.picture);
        if (iptcData != null) {
            titleProperty.setValue(iptcData.getTitle());
            captionProperty.setValue(iptcData.getCaption());
            cityProperty.setValue(iptcData.getCity());
            tagsProperty.setValue(String.join(",", iptcData.getAllTags()));
        }
    }

    private void resetIptcValues() {
        titleProperty.setValue("");
        captionProperty.setValue("");
        cityProperty.setValue("");
        tagsProperty.setValue("");
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
