package edu.swe2.cs.viewmodel;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.PhotographerModel;
import edu.swe2.cs.viewmodel.events.OnPhotographerDeleteEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerUpdateEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class PhotographerViewModel {

    private Photographer photographer;
    private PhotographerModel photographerModel;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<LocalDate> birthDate;
    private StringProperty notes;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();


    public PhotographerViewModel() {
        photographerModel = new PhotographerModel();
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        birthDate = new SimpleObjectProperty<>();
        notes = new SimpleStringProperty("");
    }

    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
        if (photographer != null) {
            updateFields();
        }
    }

    public void updateFields() {
        firstName.setValue(photographer.getFirstName());
        lastName.setValue(photographer.getLastName());
        birthDate.setValue(photographer.getBirthdate());
        notes.setValue(photographer.getNotes());
    }

    public PhotographerModel getPhotographerModel() {
        return this.photographerModel;
    }

    public StringProperty getLastName() {
        return lastName;
    }

    public StringProperty getFirstName() {
        return firstName;
    }

    public ObjectProperty<LocalDate> getBirthDate() {
        return birthDate;
    }

    public StringProperty getNotes() {
        return notes;
    }

    public boolean isValid() {
        return (isValidDate(birthDate.getValue()) && isLastName());
    }

    public boolean isLastName() {
        return (lastName.getValue() != null && !lastName.getValue().trim().isEmpty());
    }

    public boolean isValidDate(LocalDate date) {
        return (date != null && date.isBefore(LocalDate.now()));
    }

    public Photographer getPhotographer() {
        return this.photographer;
    }

    public Photographer getNewPhotographer() {
        return new Photographer(getFirstName().getValue(), getLastName().getValue(), getBirthDate().getValue(), getNotes().getValue());
    }

    public void updatePhotographer() {
        photographer.setFirstName(getFirstName().getValue());
        photographer.setLastName(getLastName().getValue());
        photographer.setBirthdate(getBirthDate().getValue());
        photographer.setNotes(getNotes().getValue());
    }

    public void OnPhotographerDelete() {
        eventBus.fire(new OnPhotographerDeleteEvent(photographer));
    }

    public void OnPhotographerUpdate() {
        eventBus.fire(new OnPhotographerUpdateEvent(getPhotographer()));
    }
}
