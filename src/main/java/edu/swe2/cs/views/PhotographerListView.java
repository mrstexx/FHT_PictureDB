package edu.swe2.cs.views;

import edu.swe2.cs.PhotographerCellFactory;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.viewmodel.PhotographerListViewModel;
import edu.swe2.cs.viewmodel.events.OnPhotographerDeleteEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerUpdateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.HashSet;
import java.util.Set;

public class PhotographerListView implements ISubscriber {

    private PhotographerListViewModel photographerListViewModel = new PhotographerListViewModel();
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();
    @FXML
    ListView<Photographer> photographerListView;


    @FXML
    private void initialize() {
        photographerListView.getItems().addAll(photographerListViewModel.getPhotographers());
        photographerListView.setCellFactory(new PhotographerCellFactory());
        photographerListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldPhotographer, newPhotographer) -> {
            photographerListViewModel.onPhotographerSelect(newPhotographer);
        });
    }

    public PhotographerListView() {
        eventBus.register(this);
    }


    //TODO: Synchronize ListView with List in Model
    // --> this is only a temporary solution
    @Override
    public void handle(IEvent<?> event) {
        if (event.getClass().equals(OnPhotographerDeleteEvent.class)) {
            Photographer photographerOfEvent = (Photographer) event.getData();
            photographerListView.getItems().remove(photographerOfEvent);
            photographerListViewModel.removePhotographerFromList(photographerOfEvent);
            if (photographerListViewModel.isEmpty()) {
                photographerListViewModel.OnIsEmpty();
            }
        } else if (event.getClass().equals(OnPhotographerUpdateEvent.class)) {
            Photographer photographerOfEvent = (Photographer) event.getData();
            for (Photographer elem : photographerListView.getItems()) {
                if (elem.getId() == photographerOfEvent.getId()) {
                    elem.setFirstName(photographerOfEvent.getFirstName());
                    elem.setLastName(photographerOfEvent.getLastName());
                    elem.setBirthdate(photographerOfEvent.getBirthdate());
                    elem.setNotes(photographerOfEvent.getNotes());
                    photographerListViewModel.updatePhotographer(photographerOfEvent);
                }
            }
        }
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPhotographerDeleteEvent.class);
        supportedEvents.add(OnPhotographerUpdateEvent.class);
        return supportedEvents;
    }
}
