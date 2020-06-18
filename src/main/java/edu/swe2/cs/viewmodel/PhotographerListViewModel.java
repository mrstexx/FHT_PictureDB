package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.viewmodel.events.OnEmptyPhotographerListEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerSelectEvent;

import java.util.List;

public class PhotographerListViewModel {

    List<Photographer> photographers;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    /**
     * Constructs a new PhotographerListViewModel. Sets photographers to a list of all stored photographers.
     *
     */
    public PhotographerListViewModel() {
        photographers = PhotographerBL.getAllPhotographers();
    }

    public List<Photographer> getPhotographers() {
        return photographers;
    }

    /**
     * Fires a new OnPhotographerSelectEvent with given photographer as parameter
     *
     * @param photographer Photographer to be passed as param to new OnPhotographerSelectEvent
     */
    public void onPhotographerSelect(Photographer photographer) {
        eventBus.fire(new OnPhotographerSelectEvent(photographer));
    }

    public void removePhotographerFromList(Photographer photographer) {
        photographers.remove((Photographer) photographer);
    }

    /**
     * @return True if photographers is empty, else False
     */
    public boolean isEmpty() {
        if (photographers.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Fires a new OnEmptyPhotographerListEvent
     *
     */
    public void OnIsEmpty() {
        eventBus.fire(new OnEmptyPhotographerListEvent());
    }

    /**
     * Updates a given photographer in the photographers list
     *
     * @param photographer Photographer to be updated
     */
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
