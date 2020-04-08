package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.PhotographerModel;
import edu.swe2.cs.viewmodel.events.OnEmptyPhotographerListEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerSelectEvent;

import java.util.List;

public class PhotographerListViewModel {

    PhotographerModel photographerModel;
    List<Photographer> photographers;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public PhotographerListViewModel() {
        photographerModel = new PhotographerModel();
        photographers = PhotographerBL.getAllPhotographers();
    }

    public List<Photographer> getPhotographers() {
        return photographers;
    }

    public void onPhotographerSelect(Photographer photographer) {
        eventBus.fire(new OnPhotographerSelectEvent(photographer));
    }

    public void removePhotographerFromList(Photographer photographer) {
        photographers.remove((Photographer) photographer);
    }

    public boolean isEmpty() {
        if (photographers.isEmpty()) {
            return true;
        }
        return false;
    }

    public void OnIsEmpty() {
        eventBus.fire(new OnEmptyPhotographerListEvent());
    }

    public void updatePhotographer(Photographer photographer) {
        for (Photographer elem : photographers) {
            if (elem.getId() == photographer.getId()) {
                elem.setFirstName(photographer.getFirstName());
                elem.setLastName(photographer.getLastName());
                elem.setBirthdate(photographer.getBirthdate());
                elem.setNotes(photographer.getNotes());
            }
        }
    }
}
