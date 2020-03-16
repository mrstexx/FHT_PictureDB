package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.Photographer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class AddPhotographerViewModel {

    private Photographer photographer;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<LocalDate> birthDate;
    private StringProperty notes;


    public AddPhotographerViewModel() {
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        birthDate = new SimpleObjectProperty<>();
        notes = new SimpleStringProperty("");
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
        return new Photographer(getFirstName().getValue(), getLastName().getValue(), getBirthDate().getValue(), getNotes().getValue());
    }
}
