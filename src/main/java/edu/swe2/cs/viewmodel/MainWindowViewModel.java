package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;

public class MainWindowViewModel {

    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    /**
     * Set first available picture from picture store
     */
    public void setFirstPicture() {
        // needed to set exif and iptc data when first time app open
        Picture firstPicture = PictureBL.getInstance().getAllPictures().get(0);
        eventBus.fire(new OnPictureSelectEvent(firstPicture));
    }

}
