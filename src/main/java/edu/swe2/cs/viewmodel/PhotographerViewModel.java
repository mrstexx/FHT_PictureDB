package edu.swe2.cs.viewmodel;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.viewmodel.events.OnPhotographerDeleteEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerUpdateEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class PhotographerViewModel {

    private Photographer photographer;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<LocalDate> birthDate;
    private StringProperty notes;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    /**
     * Constructs a new PhotographerViewModel
     */
    public PhotographerViewModel() {
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        birthDate = new SimpleObjectProperty<>();
        notes = new SimpleStringProperty("");
    }

    /**
     * Sets the photographer and updates corresponding properties
     *
     * @param photographer Photographer to be set
     */
    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
        if (photographer != null) {
            updateFields();
        }
    }

    private void updateFields() {
        firstName.setValue(photographer.getFirstName());
        lastName.setValue(photographer.getLastName());
        birthDate.setValue(photographer.getBirthdate());
        notes.setValue(photographer.getNotes());
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

    /**
     * Used to validate the photographer entity
     *
     * @return True if this photographer contains a non-empty last name and his birth date is either null or before today's date
     */
    public boolean isValid() {
        return (isValidDate(birthDate.getValue()) && isLastName());
    }

    /**
     * Used to validate photographer's last name
     *
     * @return True if this photographer contains a non-empty last name
     */
    public boolean isLastName() {
        return (lastName.getValue() != null && !lastName.getValue().trim().isEmpty());
    }

    /**
     * Used to validate photographer's birth date
     *
     * @return True if birth date is either null or before today's date
     */
    public boolean isValidDate(LocalDate date) {
        return (date == null || date.isBefore(LocalDate.now()));
    }

    public Photographer getPhotographer() {
        return this.photographer;
    }

    /**
     * Returns a new Photographer instance with set properties
     *
     * @return Photographer instance with set properties
     */
    public Photographer getNewPhotographer() {
        return new Photographer(getFirstName().getValue(), getLastName().getValue(), getBirthDate().getValue(), getNotes().getValue());
    }

    /**
     * Updates the photographer with set properties
     */
    public void updatePhotographer() {
        photographer.setFirstName(getFirstName().getValue());
        photographer.setLastName(getLastName().getValue());
        photographer.setBirthdate(getBirthDate().getValue());
        photographer.setNotes(getNotes().getValue());
    }

    /**
     * Fires a new OnPhotographerDeleteEvent with set photographer as param
     */
    public void OnPhotographerDelete() {
        eventBus.fire(new OnPhotographerDeleteEvent(photographer));
    }

    /**
     * Fires a new OnPhotographerUpdateEvent with set photographer as param
     */
    public void OnPhotographerUpdate() {
        eventBus.fire(new OnPhotographerUpdateEvent(getPhotographer()));
    }
}
